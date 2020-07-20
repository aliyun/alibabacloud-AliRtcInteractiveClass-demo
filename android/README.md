#1.前提条件  

系统版本：支持Android 4.1及以上  

API版本：不低于16  

CPU架构：支持真机架构armeabi、armeabi-v7a、arm64-v8a （不支持模拟器x86架构  

#2.运行示例项目  

参考以下步骤编译和运行示例项目：  

2.1.将代码克隆到本地  

2.2.进入项目目录  

打开 Android Studio, 点击 “Open an existing Android Studio project” 选择InteractiveClass/android  

2.3.选择真机，点击 build and run 按钮进行编译并运行。  

#3.配置参数  

 在运行Demo前，需先跑通server端的代码，然后将server的请求地址粘贴到com.aliyun.rtc.interactiveclassplayer.constant.Constant文件中的BASE_URL变量，分享地址也需要用户自己将自己的域名复制给BASE_WEB_PLAY变量。  
 
#4.项目结构说明  
 
 |-AlivcInteractiveClass_player 互动直播课功能实现库  
 
 |-AliyunVideoCommon 公共组建库  
 
 |-app 程序入口  
 
 |-thirdparty-lib 第三方库的引用  
 
#5.互动直播课功能实现库说明  
 
 |-adapter  
 
   |-BottomFunctionAdapter  底部功能按钮列表adapter  
 
   |-StudentListAdapter  学生列表adapter  
 
 |-bean  
 
 |-Constant 常量数据管理类，在这配置server端请求域名和分享链接的域名  
 
 |-network 网络请求  
 
 |-play 阿里云播放器  
 
 |-rtc RTC  
 
 |-ui 放的互动界面和登陆界面  
 
 |-util 工具类  
 
 |-view 自定义view  
 
#6.学生身份登陆和上下麦逻辑  

  学生登陆是通过从server端获取播放链接，然后用播放器sdk加载；上麦操作是先停止播放并调用rtc sdk以互动模式加入屏道；上下麦就是切换两个展示的surface来实现的  
  
#7.大小流切换  

  大小流切换类似于上下麦操作，通过切换两个surfaceview的位置，显示在主屏或者小屏  


