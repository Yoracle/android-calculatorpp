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

import android.util.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 10/11/12
 * Time: 12:15 AM
 */
public class AndroidCalculatorLogger implements CalculatorLogger {

	@Nonnull
	private static final String TAG = "Calculatorpp";

	@Override
	public void debug(@Nullable String tag, @Nonnull String message) {
		Log.d(getTag(tag), message);
	}

	@Nonnull
	private String getTag(@Nullable String tag) {
		return tag != null ? TAG + "/" + tag : TAG;
	}

	@Override
	public void debug(@Nullable String tag, @Nullable String message, @Nonnull Throwable e) {
		Log.d(getTag(tag), message, e);
	}

	@Override
	public void error(@Nullable String tag, @Nullable String message, @Nonnull Throwable e) {
		Log.e(getTag(tag), message, e);
	}

	@Override
	public void error(@Nullable String tag, @Nullable String message) {
		Log.e(getTag(tag), message);
	}
}
