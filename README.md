# EyesTrackDemo
Camera2Fragment人眼跟踪移植流程

**1.文件添加**

拷贝AutoFitTextureView.java，Camera2BasicFragment.java至java目录下合适包中。
拷贝fragment_camera2_basic.xml至res目录下合适位置，一般为layout目录下。
建议.java直接在工程中拷贝，如果在文件管理器内拷贝要注意修改包名；.xml建议直接新建再复制内容。


**2.对照修改AndroidManifest.xml**

添加可能用上的权限说明。
```
<!-- camera2 -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera2" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```

**3.修改fragment_camera2_basic.xml**

修改包名com.dhht.serialportutil.AutoFitTextureView为当前包名

**4.对照修改最上层显示xml**

选择最顶层图片/视频显示用的Activity对应的xml文件，一般小工程直接为activity_main.xml。
目的是将Camera2BasicFragment作为一个顶层的透明的fragment运行，为fragment创建合适的UI容器。Fragment必须附属于一个Activity，一般选择最顶层图片/视频显示用的Activity，其生命周期随最顶层Activity。
注意：选择的Activity在显示期间最好的一直保持在Running状态的，如果没设置后台运行，一旦有其他的Activity入栈了，Activity转为Pause或者Stop状态时，人眼跟踪服务将不可用，而且有被动销毁的风险。
创建合适的fragment容器：
```
<!-- 人眼跟踪 -->
<FrameLayout
    android:id="@+id/container"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="container" />
```

**5.修改其他配置xml**

在string.xml添加：
```
<string name="request_permission">This sample needs camera permission.</string>
<string name="camera_error">This device doesn\'t support Camera2 API.</string>
```
不是很重要，只是为了解决一点报错，可以直接在报错点修改。

**6.添加依赖库**

此时Camera2BasicFragment.java一般会报import android.support.v13.app.FragmentCompat;的错误,添加下面的依赖库：
添加完成会自动重新编译，如果重新编译成功（可能会需要同步版本），说明添加依赖成功。
如果报一些xml错误，说明版本不对应，比如上图的依赖包版本是25.0.0，需要在build.gradle修改依赖库版本保持和API匹配及工程自身依赖包匹配。
```
android {
    ...
    defaultConfig {
        ...
        //最小SDK要21以上
        minSdkVersion 21
        ...
     }
    ...
}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    //自身包版本
    implementation 'com.android.support:appcompat-v7:25.0.0'
    ...
    //添加包版本
    implementation 'com.android.support:support-v4:25.0.0'
    implementation 'com.android.support:support-v13:25.0.0'
    implementation 'com.android.support:cardview-v7:25.0.0'
}
```

**7.外部调用**
在xml对应的顶层Activity上使用如下代码调用
```
//人眼跟踪
if (null == savedInstanceState) {
    getFragmentManager().beginTransaction()
            .replace(R.id.container, Camera2BasicFragment.newInstance())
            .commit();
}
```

在xml对应的顶层Activity实现接口回调
```
implements Camera2BasicFragment.FragmentInteraction

private int eyesLocation = 0;
@Override
public void process(int location) {
    eyesLocation = location;
}
```

**8.权限开启**
如果第一次开启软件没有及时打开权限，记得在系统软件管理里打开。
