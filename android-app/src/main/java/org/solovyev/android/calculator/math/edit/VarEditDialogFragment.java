/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.calculator.math.edit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import jscl.math.function.IConstant;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.solovyev.android.Views;
import org.solovyev.android.calculator.*;
import org.solovyev.android.calculator.model.Var;
import org.solovyev.android.sherlock.AndroidSherlockUtils;
import org.solovyev.common.text.Strings;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

/**
 * User: Solovyev_S
 * Date: 01.10.12
 * Time: 17:41
 */
public class VarEditDialogFragment extends DialogFragment implements CalculatorEventListener {

	private final static List<Character> acceptableChars = Arrays.asList(Strings.toObjects("1234567890abcdefghijklmnopqrstuvwxyzйцукенгшщзхъфывапролджэячсмитьбюёαβγδεζηθικλμνξοπρστυφχψω_".toCharArray()));

	@Nonnull
	private final Input input;

	public VarEditDialogFragment() {
		this(Input.newInstance());
	}

	public VarEditDialogFragment(@Nonnull Input input) {
		this.input = input;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.var_edit, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();

		Locator.getInstance().getCalculator().addCalculatorEventListener(this);
	}

	@Override
	public void onPause() {
		Locator.getInstance().getCalculator().removeCalculatorEventListener(this);

		super.onPause();
	}

	@Override
	public void onViewCreated(@Nonnull View root, Bundle savedInstanceState) {
		super.onViewCreated(root, savedInstanceState);

		final String errorMsg = this.getString(R.string.c_char_is_not_accepted);

		final EditText editName = (EditText) root.findViewById(R.id.var_edit_name);
		editName.setText(input.getName());
		editName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if (!acceptableChars.contains(c)) {
						s.delete(i, i + 1);
						Toast.makeText(getActivity(), String.format(errorMsg, c), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		processGreekButtons(root, editName, R.id.var_edit_greek_buttons_1);
		processGreekButtons(root, editName, R.id.var_edit_greek_buttons_2);

		// show soft keyboard automatically
		editName.requestFocus();
		getDialog().getWindow().setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);

		final EditText editValue = (EditText) root.findViewById(R.id.var_edit_value);
		editValue.setText(input.getValue());

		final EditText editDescription = (EditText) root.findViewById(R.id.var_edit_description);
		editDescription.setText(input.getDescription());

		final Var.Builder varBuilder;
		final IConstant constant = input.getConstant();
		if (constant != null) {
			varBuilder = new Var.Builder(constant);
		} else {
			varBuilder = new Var.Builder();
		}

		root.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		root.findViewById(R.id.save_button).setOnClickListener(new VarEditorSaver<IConstant>(varBuilder, constant, root, Locator.getInstance().getEngine().getVarsRegistry(), this));

		if (constant == null) {
			// CREATE MODE
			getDialog().setTitle(R.string.c_var_create_var);

			root.findViewById(R.id.remove_button).setVisibility(View.GONE);
		} else {
			// EDIT MODE
			getDialog().setTitle(R.string.c_var_edit_var);

			root.findViewById(R.id.remove_button).setOnClickListener(MathEntityRemover.newConstantRemover(constant, null, getActivity(), VarEditDialogFragment.this));
		}
	}

	private void processGreekButtons(@Nonnull View root, @Nonnull final EditText editName, int greekButtonsViewId) {
		final ViewGroup greekButtons = (ViewGroup) root.findViewById(greekButtonsViewId);
		Views.processViewsOfType(greekButtons, Button.class, new Views.ViewProcessor<Button>() {
			@Override
			public void process(@Nonnull final Button greekButton) {
				greekButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						editName.append(greekButton.getText());
					}
				});
			}
		});
	}

	@Override
	public void onCalculatorEvent(@Nonnull CalculatorEventData calculatorEventData, @Nonnull CalculatorEventType calculatorEventType, @Nullable Object data) {
		switch (calculatorEventType) {
			case constant_removed:
			case constant_added:
			case constant_changed:
				if (calculatorEventData.getSource() == this) {
					dismiss();
				}
				break;

		}
	}

	/*
	**********************************************************************
	*
	*                           STATIC
	*
	**********************************************************************
	*/

	public static void showDialog(@Nonnull Input input, @Nonnull FragmentManager fm) {
		AndroidSherlockUtils.showDialog(new VarEditDialogFragment(input), "constant-editor", fm);
	}

	public static class Input {

		@Nullable
		private IConstant constant;

		@Nullable
		private String name;

		@Nullable
		private String value;

		@Nullable
		private String description;

		private Input() {
		}

		@Nonnull
		public static Input newInstance() {
			return new Input();
		}

		@Nonnull
		public static Input newFromConstant(@Nonnull IConstant constant) {
			final Input result = new Input();
			result.constant = constant;
			return result;
		}

		@Nonnull
		public static Input newFromValue(@Nullable String value) {
			final Input result = new Input();
			result.value = value;
			return result;
		}

		@Nonnull
		public static Input newInstance(@Nullable IConstant constant, @Nullable String name, @Nullable String value, @Nullable String description) {
			final Input result = new Input();
			result.constant = constant;
			result.name = name;
			result.value = value;
			result.description = description;
			return result;
		}

		@Nullable
		public IConstant getConstant() {
			return constant;
		}

		@Nullable
		public String getName() {
			return name == null ? (constant == null ? null : constant.getName()) : name;
		}

		@Nullable
		public String getValue() {
			return value == null ? (constant == null ? null : constant.getValue()) : value;
		}

		@Nullable
		public String getDescription() {
			return description == null ? (constant == null ? null : constant.getDescription()) : description;
		}
	}
}
