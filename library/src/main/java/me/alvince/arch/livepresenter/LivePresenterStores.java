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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import java.util.Arrays;

/**
 * Created by alvince on 2018/11/30
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/12/4
 * @since 0.1.0
 */
public class LivePresenterStores {

    private static final long HANDLE_GC_DELAYED = 3000L;

    private static final int HANDLE_GC_PERFORM = 772;
    private static final int HANDLE_GC_POST = 198;

    private static boolean sGarbage = false;

    private static int[] sStoresKeyArr = new int[16];
    private static SparseArray<LivePresenterStore> sStoresArray = new SparseArray<>();

    private static Handler sStoresHandler = new StoresHandler();

    private LivePresenterStores() {
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

    static void gc() {
        if (sStoresArray.size() > 0) {
            sStoresHandler.sendEmptyMessage(HANDLE_GC_POST);
        }
    }

    private static LivePresenterStore ofLifecycle(LifecycleOwner owner) {
        LivePresenterStore store = new LivePresenterStore(owner);
        sStoresArray.put(owner.hashCode(), store);
        putInstance(owner.hashCode(), store);
        return store;
    }

    private static void putInstance(int key, LivePresenterStore instance) {
        int curSize = sStoresArray.size();

        // garbage clear
        if (sGarbage) {
            int[] swap = new int[(int) (curSize * 1.5F)];
            int pos = 0;
            for (int k : sStoresKeyArr) {
                LivePresenterStore s = sStoresArray.get(k);
                if (s != null) {
                    swap[pos++] = k;
                }
            }
            sStoresKeyArr = swap;
            sGarbage = false;
        }

        // Expand capacity
        if (sStoresKeyArr.length == curSize) {
            sStoresKeyArr = Arrays.copyOf(sStoresKeyArr, (int) (curSize * 1.5F));
        }

        sStoresArray.put(key, instance);
        sStoresKeyArr[curSize] = key;
    }

    private static void performGC() {
        if (sStoresArray.size() > 0) {
            for (int i = 0; i < sStoresKeyArr.length; i++) {
                int k = sStoresKeyArr[i];
                LivePresenterStore store = sStoresArray.get(k);
                if (store != null && !store.isActived()) {
                    sStoresArray.remove(k);
                    sStoresKeyArr[i] = 0;
                    sGarbage = true;
                }
            }
        }
    }

    /**
     * Stores internal handler
     */
    private static class StoresHandler extends Handler {
        StoresHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_GC_POST:
                    removeCallbacksAndMessages(null);
                    sendEmptyMessageDelayed(HANDLE_GC_PERFORM, HANDLE_GC_DELAYED);
                    break;
                case HANDLE_GC_PERFORM:
                    performGC();
                default:
            }
        }
    }
}
