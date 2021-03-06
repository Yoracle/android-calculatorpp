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

package org.solovyev.android.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 9/25/12
 * Time: 10:31 PM
 */
public interface CalculatorActivityHelper {

	void onCreate(@Nonnull SherlockFragmentActivity activity, @Nullable Bundle savedInstanceState);

	void onCreate(@Nonnull Activity activity, @Nullable Bundle savedInstanceState);

	void onSaveInstanceState(@Nonnull SherlockFragmentActivity activity, @Nonnull Bundle outState);

	void onSaveInstanceState(@Nonnull Activity activity, @Nonnull Bundle outState);

	int getLayoutId();

	@Nonnull
	CalculatorPreferences.Gui.Theme getTheme();

	@Nonnull
	CalculatorPreferences.Gui.Layout getLayout();

	void onResume(@Nonnull SherlockFragmentActivity activity);

	void onResume(@Nonnull Activity activity);

	void onPause(@Nonnull Activity activity);

	void onPause(@Nonnull SherlockFragmentActivity activity);

	void onDestroy(@Nonnull SherlockFragmentActivity activity);

	void onDestroy(@Nonnull Activity activity);

	void addTab(@Nonnull SherlockFragmentActivity activity,
				@Nonnull String tag,
				@Nonnull Class<? extends Fragment> fragmentClass,
				@Nullable Bundle fragmentArgs,
				int captionResId,
				int parentViewId);

	void addTab(@Nonnull SherlockFragmentActivity activity,
				@Nonnull CalculatorFragmentType fragmentType,
				@Nullable Bundle fragmentArgs,
				int parentViewId);

	void setFragment(@Nonnull SherlockFragmentActivity activity,
					 @Nonnull CalculatorFragmentType fragmentType,
					 @Nullable Bundle fragmentArgs,
					 int parentViewId);


	void logDebug(@Nonnull String message);

	void processButtons(@Nonnull Activity activity, @Nonnull View root);

	void logError(@Nonnull String message);

	void selectTab(@Nonnull SherlockFragmentActivity activity, @Nonnull CalculatorFragmentType fragmentType);
}
