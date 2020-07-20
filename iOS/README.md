## Demo 运行指引
### 1 前提条件
#### 开发前的环境要求如下表所示，详情请参见使用限制。

| 类别 | 说明 |
| -------- | -------- |
| iPhone设备     | 支持iPhone5及以上     |
| 系统版本     | 支持iOS 8.0及以上     |
| CPU架构     | 支持真机架构armv7+arm64，不支持模拟器i386、x86架构     |
| Xcode版本     | 支持Xcode9.0及以上     |
| 其他     | 不支持Bitcode，不支持屏幕旋转     |


```
说明 您需要持有Apple开发证书或个人账号。
```

### 2 获取 Demo
```
说明 Demo源码中已经集成AliRTC SDK（版本：1.17.9）。SDK集成方式通过CocoaPods集成。
```


### 3 运行项目
#### 运行步骤如下：
1、 打开终端 cd到iOS文件夹下 执行 pod install

2、 双击打开 iOS 目录下的 LectureHall.xcworkspace

3、选择运行 Target 为 LectureHall

4、 修改Bundle Identifier 和 开发者证书

5、 点击 build and run 按钮进行编译和运行。

### 4 快速跑通

为了快速在自己的demo中调用服务端的接口，需要您在本地运行服务端的代码，在AppConfig.h中填入您的服务端地址

### 5 项目结构说明

-Utils:工具类
-Main:上课页面
-Manager:用管理类
-Login:登录页面


1 创建并加入频道
```
    //1.实例化AliRtcEngine 并设置代理
   _engine = [AliRtcEngine sharedInstance:self extras:@""];
   
   //2.获取授权信息
   //AliRtcAuthInfo:各项参数均需要客户App Server(客户的server端) 通过OpenAPI来获取，然后App Server下发至客户端，客户端将各 项参数赋值后，即可joinChannel
   
   AuthInfo *authInfo = "从server端获取"
   
  [_engine  joinChannel:authInfo name:name onResult:^(NSInteger errCode) {
                    //加入频道回调处理
                    if (errCode == 0) {
                        //加入房间成功
                    } else {
                     //加入房间失败
                    }
                }];
```
2 离开频道
```
        //离开频道
        [self.engine leaveChannel];
     //销毁SDK实例
     [AliRtcEngine destroy];
```
3 开启本地预览
```
 [self.engine startPreview];
 ```
4 停止伴奏
```
 [self.engine stopAudioEffectWithSoundId:soundId];
```
5 停止本地预览
```
 [self.engine stopPreview];
 ```
6 切换摄像头
```
 [self.engine switchCamera];
```
7 静音
```
[self.engine muteLocalMic:YES];
```
8 取消静音
```
[self.engine muteLocalMic:NO];
```
9 开启扬声器
```
[self.engine enableSpeakerphone:YES];
```
10 关闭扬声器
```
[self.engine enableSpeakerphone:NO];
```
11 为远端的视频设置窗口以及绘制参数
```
[self.engine setRemoteViewConfig:canvas uid:uid forTrack:track];
```
12 为本地预览设置窗口以及绘制参数
```
[self.engine setLocalViewConfig:viewConfig forTrack:track];
```
