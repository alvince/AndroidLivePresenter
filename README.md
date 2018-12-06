Android Live Presenter
===

<a href="#">
    <img src="https://img.shields.io/badge/AndroidLivePresenter-0.1-green.svg" alt="Release" />
</a>
<a href="https://opensource.org/licenses/MIT/">
    <img src="https://img.shields.io/badge/License-MIT-red.svg" alt="License" />
</a>
<a href="https://bintray.com/alvince-zy/android/android-live-presenter">
    <img src="https://img.shields.io/badge/JCenter-0.1-brightgreen.svg" alt="Bintray" />
</a>

An android presenter bundle, with lifecycle observable that prevent leaks from context.  

参考 __Android-ViewModel__ 封装的 `Presenter` 组件  
专注业务逻辑，自动处理生命周期的依赖，上下文对象销毁时自动释放 View 对象  

## 添加依赖

```Groovy
dependencies {
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
    implementation 'me.alvince.android:android-live-presenter:0.1'
}
```

## Sample

#### 定义 Presenter

Java

```Java
public class SamplePresenter extends LivePresenter<ISampleView> {
    …

    @Override
    public void onCleared() {
        // 视图销毁时清理
        …
    }
}
```

Kotlin

```Kotlin
class SamplePresenter : LivePresenter<ISampleView>() {
    …

    override fun onCleared() {
        …
    }
}
```

#### View

创建 `Presenter`

Java

```Java
public class SampleActivity extends AppCompatActivity implements ISampleView {

    private SamplePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        …
        mPresenter = LivePresenterProviders.of(this).get(SamplePresenter.class);
        mPresenter.bindView(this);
    }
}
```

Kotlin

```Kotlin
class SampleActivity : AppCompatActivity(), ISampleView {

    private val presenter by lazy { LivePresenterProviders.of(this).get(SamplePresenter::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        …
        presenter.bindViewport(this)
    }
}
```

## LICENSE
```
Copyright (c) 2018 alvince

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```