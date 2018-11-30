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

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;

import java.util.HashMap;

/**
 * Created by alvince on 2018/11/30
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/11/30
 * @since 0.1.0
 */
public class LivePresenterStore {

    private final HashMap<String, LivePresenter> mMap = new HashMap<>();

    private LifecycleObserver mLifecycleObserver;

    LivePresenterStore(LifecycleOwner owner) {
        observeLifecycle(owner);
    }

    final void put(String key, LivePresenter presenter) {
        LivePresenter oldViewModel = mMap.put(key, presenter);
        if (oldViewModel != null) {
            oldViewModel.onCleared();
        }
    }

    final LivePresenter get(String key) {
        return mMap.get(key);
    }

    /**
     * Clears internal storage and notifies LivePresenters that they are no longer used.
     */
    public final void clear() {
        for (LivePresenter presenter : mMap.values()) {
            presenter.onCleared();
            presenter.mViewport = null;
        }
        mMap.clear();
    }

    private void observeLifecycle(LifecycleOwner lifecycleOwner) {
        if (mLifecycleObserver == null) {
            mLifecycleObserver = new GenericLifecycleObserver() {
                @Override
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    switch (event) {
                        case ON_DESTROY:
                            source.getLifecycle().removeObserver(this);
                            clear();
                        default:
                    }
                }
            };
        }
        lifecycleOwner.getLifecycle().addObserver(mLifecycleObserver);
    }
}
