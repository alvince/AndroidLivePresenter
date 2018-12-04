package me.alvince.arch.livepresenter.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.alvince.arch.livepresenter.LivePresenterProviders
import me.alvince.arch.livepresenter.sample.home.IHomeView
import me.alvince.arch.livepresenter.sample.home.presenter.MainPresenter

/**
 * Created by alvince on 2018/12/3
 *
 * @author alvince.zy@gmail.com
 */
class MainActivity : AppCompatActivity(), IHomeView {

    private val presenter by lazy { LivePresenterProviders.of(this).get(MainPresenter::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.bindViewport(this)
    }
}