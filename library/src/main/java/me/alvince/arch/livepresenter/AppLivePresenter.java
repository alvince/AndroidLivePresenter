package me.alvince.arch.livepresenter;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by alvince on 2018/11/30
 *
 * @author alvince.zy@gmail.com
 * @version 0.1.0, 2018/11/29
 * @since 0.1.0
 */
public class AppLivePresenter<V> extends LivePresenter<V> {

    private Application mApplication;

    public AppLivePresenter(@NonNull Application application) {
        super();
        mApplication = application;
    }

    /**
     * Return the application.
     */
    @SuppressWarnings("TypeParameterUnusedInFormals")
    @NonNull
    public <T extends Application> T getApplication() {
        //noinspection unchecked
        return (T) mApplication;
    }
}
