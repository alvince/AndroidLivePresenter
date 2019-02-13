/*
 * Copyright (c) 2018 alvince
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.alvince.arch.livepresenter;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import me.alvince.arch.livepresenter.LivePresenterProvider.Factory;

/**
 * Created by alvince on 2018/11/29
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/11/29
 * @since 0.1.0
 */
public class LivePresenterProviders {

    private LivePresenterProviders() {
    }

    public static LivePresenterProvider of(@NonNull FragmentActivity activity) {
        return of(activity, null);
    }

    @NonNull
    @MainThread
    public static LivePresenterProvider of(@NonNull FragmentActivity activity, @Nullable Factory factory) {
        Application application = checkApplication(activity);
        if (factory == null) {
            factory = LivePresenterProvider.AndroidLivePresenterFactory.getInstance(application);
        }
        return new LivePresenterProvider(LivePresenterStores.of(activity), factory);
    }

    @NonNull
    @MainThread
    public static LivePresenterProvider of(@NonNull Fragment fragment) {
        return of(fragment, null);
    }

    @NonNull
    @MainThread
    public static LivePresenterProvider of(@NonNull Fragment fragment, @Nullable Factory factory) {
        Application application = checkApplication(checkActivity(fragment));
        if (factory == null) {
            factory = LivePresenterProvider.AndroidLivePresenterFactory.getInstance(application);
        }
        return new LivePresenterProvider(LivePresenterStores.of(fragment), factory);
    }

    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request LivePresenter before onCreate call.");
        }
        return application;
    }

    private static Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create LivePresenterProvider for detached fragment");
        }
        return activity;
    }
}
