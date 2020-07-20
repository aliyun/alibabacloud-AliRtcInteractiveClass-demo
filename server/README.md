解压dist/InteractiveClass.zip

修改配置文件 BOOT-INF/classes/application.properties

修改 rtc 应用appId、appKey 
accessKeyID=*****
accessKeySecret=*****

设置AK，需要添加 AliyunRTCFullAccess 权限 
rtc.liveclass.appId = *****
rtc.liveclass.appKey = *****

播流域名和url鉴权key
live_artp_play_domain = *****
live_artp_play_domain_auth_key = *****

播流域名和url鉴权key
live_play_domain = *****
live_play_domain_auth_key = *****

推流域名和url鉴权key
live_push_domain = *****
live_push_domain_auth_key = *****

布局id
layout=*****


执行restart.sh即可重启appserver。

访问http://127.0.0.1:8080/interactive-live-class/index.html即可看到启动成功提示。



