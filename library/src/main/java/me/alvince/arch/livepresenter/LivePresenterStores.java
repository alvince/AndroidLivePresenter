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

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

/**
 * Created by alvince on 2018/11/30
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/11/30
 * @since 0.1.0
 */
public class LivePresenterStores {

    private static SparseArray<LivePresenterStore> sStoresArray;

    private LivePresenterStores() {
        sStoresArray = new SparseArray<>();
    }

    /**
     * Returns the {@link LivePresenterStore} of the given activity.
     *
     * @param activity an activity whose {@code ViewModelStore} is requested
     * @return a {@code LivePresenterStore}
     */
    @NonNull
    @MainThread
    public static LivePresenterStore of(@NonNull FragmentActivity activity) {
        return ofLifecycle(activity);
    }

    @NonNull
    @MainThread
    public static LivePresenterStore of(@NonNull Fragment fragment) {
        return ofLifecycle(fragment);
    }

    private static LivePresenterStore ofLifecycle(LifecycleOwner owner) {
        LivePresenterStore store = new LivePresenterStore(owner);
        sStoresArray.put(owner.hashCode(), store);
        return store;
    }
}
