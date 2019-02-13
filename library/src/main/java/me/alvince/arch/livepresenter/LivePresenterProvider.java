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

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by alvince on 2018/11/29
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/11/30
 * @since 0.1.0
 */
public class LivePresenterProvider {

    /**
     * Implementations of `Factory` interface are responsible to instantiate LivePresenters.
     */
    interface Factory {
        /**
         * Creates a new instance of the given `Class`.
         *
         * @param presenterClass a `Class` whose instance is requested
         * @param <T>            The type parameter for the Presenter.
         * @return a newly created Presenter
         * </T>
         */
        @NonNull
        <T extends LivePresenter> T create(Class<T> presenterClass);
    }

    private static final String DEFAULT_KEY = "me.alvince.arch.livepresenter.LivePresenterProvider.DefaultKey";

    private final Factory mFactory;
    private final LivePresenterStore mPresenterStore;

    public LivePresenterProvider(@NonNull LivePresenterStore store, @NonNull Factory factory) {
        mFactory = factory;
        mPresenterStore = store;
    }

    @NonNull
    @MainThread
    public <P extends LivePresenter> P get(@NonNull Class<P> presenterClass) {
        String canonicalName = presenterClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be LivePresenters");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, presenterClass);
    }

    @NonNull
    @MainThread
    public <T extends LivePresenter> T get(@NonNull String key, @NonNull Class<T> modelClass) {
        LivePresenter presenter = mPresenterStore.get(key);

        if (modelClass.isInstance(presenter)) {
            //noinspection unchecked
            return (T) presenter;
        } else {
            //noinspection StatementWithEmptyBody
            if (presenter != null) {
                // TODO: log a warning.
            }
        }

        presenter = mFactory.create(modelClass);
        mPresenterStore.put(key, presenter);
        //noinspection unchecked
        return (T) presenter;
    }

    /**
     * Simple factory, which calls empty constructor on the give class.
     */
    public static class NewInstanceFactory implements Factory {

        @SuppressWarnings("ClassNewInstance")
        @NonNull
        @Override
        public <T extends LivePresenter> T create(@NonNull Class<T> modelClass) {
            //noinspection TryWithIdenticalCatches
            try {
                return modelClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
    }

    /**
     * {@link Factory} which may create {@link LivePresenter}, which have an empty constructor.
     */
    public static class AndroidLivePresenterFactory extends LivePresenterProvider.NewInstanceFactory {

        private static AndroidLivePresenterFactory sInstance;

        /**
         * Retrieve a singleton instance of AndroidLivePresenterFactory.
         *
         * @param application an application to pass in {@link LivePresenter}
         * @return A valid {@link AndroidLivePresenterFactory}
         */
        @NonNull
        public static AndroidLivePresenterFactory getInstance(@NonNull Application application) {
            if (sInstance == null) {
                sInstance = new AndroidLivePresenterFactory(application);
            }
            return sInstance;
        }

        private Application mApplication;

        /**
         * Creates a {@code AndroidLivePresenterFactory}
         *
         * @param application an application to pass in {@link LivePresenter}
         */
        public AndroidLivePresenterFactory(@NonNull Application application) {
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends LivePresenter> T create(@NonNull Class<T> modelClass) {
            if (AppLivePresenter.class.isAssignableFrom(modelClass)) {
                //noinspection TryWithIdenticalCatches
                try {
                    return modelClass.getConstructor(Application.class).newInstance(mApplication);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                }
            }
            return super.create(modelClass);
        }
    }
}
