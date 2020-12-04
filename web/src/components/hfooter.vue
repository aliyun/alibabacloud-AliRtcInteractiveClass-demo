<template>
  <div class="hfooter">
    <div class="function" :class="{'teacher':$store.state.data.role==0}">
      <span class="vsersion">{{$store.state.data.version}}</span>
      <div
        class="mic"
        :class="[{'on':isMuteLocalMic},{'visiable':!$store.state.data.isInRTC}]"
        @click="muteLocalMic"
        :disable="$store.state.data.isInRTC"
      >
        <i
          :style="isMuteLocalMic ? 'background-image:url('+  micOff +')' : 'background-image:url('+  micOn +')'"
        ></i>
        <span>{{isMuteLocalMic?'取消静音':'静音'}}</span>
      </div>
      <div
        class="camera"
        :class="[{'on':isMuteLocalCamera},{'visiable':!$store.state.data.isInRTC}]"
        @click="muteLocalCamera"
        :disable="$store.state.data.isInRTC"
      >
        <i
          :style="isMuteLocalCamera ? 'background-image:url('+  cameraOff +')' : 'background-image:url('+  cameraOn +')'"
        ></i>
        <span>{{isMuteLocalCamera?'打开摄像头':'关闭摄像头'}}</span>
      </div>
      <!-- <div
        class="startclass"
        :class="{'initstate': isTeacherInitState,'on':!$store.state.data.isPublish}"
        @click="startPublish"
        v-if="$store.state.data.role==0"
      >
        <i :style="$store.state.data.isPublish ? 'background-image:url('+ stop +')' : 'background-image:url('+  start +')'"></i>
        <span>{{isTeacherInitState ? '开始课程' : $store.state.data.isPublish?'暂停课程':'恢复课程'}}</span>
      </div>-->
      <div class="screen" @click="publishScreen" v-if="$store.state.data.supportInfo.browser!='Safari'&&$store.state.data.isInRTC">
        <i :style="'background-image:url(' + screenshare + ')'"></i>
        <span>{{$store.state.data.isPublishScreen?'取消屏幕共享':'屏幕共享'}}</span>
      </div>
      <div
        class="switchRole"
        :class="{'on':$store.state.data.isInRTC}"
        v-if="$store.state.data.role==1"
        @click="switchRole"
      >
        <i
          :style="$store.state.data.isInRTC ? 'background-image:url('+ switchOff +')' : 'background-image:url('+  switchOn +')'"
        ></i>
        <span>{{$store.state.data.isInRTC?'取消连麦':'连麦'}}</span>
      </div>
      <div class="share" @click="shareUrlCopy">
        <i :style="'background-image:url('+ share +')'"></i>
        <span>分享</span>
      </div>
      <div class="leaveclass" @click="exitRoom">
        <i
          :style="isTeacherInitState&&$store.state.data.role==0 ? 'background-image:url('+ leave +')' : 'background-image:url('+ end +');background-color: rgba(245,34,45,0.15)'"
        ></i>
        <span>{{isTeacherInitState&&$store.state.data.role==0?'结束课程':'离开教室'}}</span>
      </div>
      <input
        id="share"
        type="text"
        :value="`https://alivc-demo-cms.alicdn.com/html-demo/play-class/index.html#/studentOnly?channel=${$store.state.data.classNum}&role=1`"
      />
    </div>
    <hmodal v-if="exitRoomToast"></hmodal>
  </div>
</template>
<script>
import RTCClient from "../core/rtc-client";
import AliPlayClient from "../core/aliplay-client";
import Util from "../core/utils/utils";
import http from "../core/http/http";
export default {
  data() {
    return {
      isTeacherInitState: false, // 教师初始状态
      isMuteLocalMic: RTCClient.instance.isMuteLocalMic,
      isMuteLocalCamera: RTCClient.instance.isMuteLocalCamera,
      micOn: require("../assets/img/mic-on.png"),
      micOff: require("../assets/img/mic-off.png"),
      cameraOn: require("../assets/img/camera-on.png"),
      cameraOff: require("../assets/img/camera-off.png"),
      start: require("../assets/img/start.png"),
      stop: require("../assets/img/stop.png"),
      switchOn: require("../assets/img/switch-on.png"),
      switchOff: require("../assets/img/switch-off.png"),
      screenshare: require("../assets/img/screen.png"),
      share: require("../assets/img/share.png"),
      leave: require("../assets/img/leave.png"),
      end: require("../assets/img/end.png"),
      exitRoomToast: false,
      isMobile: Util.isMobile()
    };
  },
  created() {},
  methods: {
    /**
     *
     */
    muteLocalMic() {
      if (!RTCClient.instance.muteLocalMic()) {
        this.showToast();
        return;
      }
      this.isMuteLocalMic = RTCClient.instance.isMuteLocalMic;
    },
    /**
     *
     */
    muteLocalCamera() {
      if (!this.$store.state.data.isInRTC) {
        this.showToast();
        return;
      }
      RTCClient.instance
        .muteLocalCamera(document.getElementById("localVideo"))
        .then(re => {
          this.isMuteLocalCamera = RTCClient.instance.isMuteLocalCamera;
          hvuex({ isPreview: RTCClient.instance.isPreview });
          if(RTCClient.instance.isPreview){
              AppConfig.localStream=document.getElementById("localVideo").srcObject;
              document.getElementById(RTCClient.instance.userId).srcObject = document.getElementById("localVideo").srcObject;
          }
        })
        .catch(err => {});
    },
    /**
     * 推屏幕流
     */
    publishScreen() {
      if (!RTCClient.instance.isPublish) {
        this.showToast();
        return;
      }
      if (this.$store.state.data.isPublishScreen) {
        RTCClient.instance
          .stopPublishScreen()
          .then(re => {
            http.updateMPUTask(
              this.$store.state.data.classNum,
              RTCClient.instance.userId,
              "camera"
            );
            RTCClient.instance.stopScreenSharePreview(document.getElementById("localVideo"));
            document.getElementById("localVideo").srcObject = AppConfig.localStream;
            if(document.getElementById(RTCClient.instance.userId).srcObject == AppConfig.localStream){
              document.getElementById(RTCClient.instance.userId).srcObject = null;
            }
            hvuex({ isPublishScreen: false });
          })
          .catch(err => {
          });
      } else {
        RTCClient.instance
          .publishScreen()
          .then(() => {
            http.updateMPUTask(
              this.$store.state.data.classNum,
              RTCClient.instance.userId,
              "shareScreen"
            );
            hvuex({ isPublishScreen: true });
            if(this.$store.state.data.isSwitchScreen){
              RTCClient.instance.startScreenSharePreview(document.getElementById(RTCClient.instance.userId));
            }else{
              RTCClient.instance.startScreenSharePreview(document.getElementById("localVideo"));
              document.getElementById(RTCClient.instance.userId).srcObject = AppConfig.localStream;
            }
          })
          .catch(err => {
          });
      }
    },
    /**
     * 切换上下麦
     */
    switchRole() {
      if (!this.$store.state.data.isInRTC) {
        http.describeChannelUsers(this.$store.state.data.classNum).then(re => {
          if (re.data.commTotalNum == 0) {
             this.$alert("需要web端老师发起课程后，才能进入课程", "", {
             confirmButtonText: '确定',
             callback: action => {}
            });
            return false;
          }
          document.getElementById("player-con")
            ? document.getElementById("player-con").remove()
            : "";
          this.registerCallBack();
          RTCClient.instance.configLocalScreenPublish=false;
          RTCClient.instance
            .switchRoleToInteractive()
            .then(userId => {
              hvuex({ isInRTC: true, isSwitchScreen: false, userId: userId });
              this.$nextTick(() => {
                player.dispose();
                Util.startPreview();
              });
            })
            .catch(err => {});
        });
      } else {
        RTCClient.instance.leaveChannel().finally(() => {
          hvuex({
            isPublishScreen: false,
            isPublish: false,
            isPreview: RTCClient.instance.isPreview,
            isInRTC: false,
            isSwitchScreen: true
          });
          this.$nextTick(() => {
            AliPlayClient.palyVideo(this.$store.state.data.classNum);
          });
        });
        this.isMuteLocalMic = false;
        this.isMuteLocalCamera = false;
      }
    },
    /**
     * 注册回调
     */
    registerCallBack() {
      RTCClient.instance.registerCallBack((eventName, data) => {
        switch (eventName) {
          case "onJoin":
          case "onPublisher":
          case "onUnPublisher":
          case "onNotify":
            hvuex({ userList: RTCClient.instance.getUserList() });
            break;
          case "onSubscribeResult":
            Util.showRemoteVideo(data);
            break;
          case "onError":
            Util.showErrorMsg(data);
            break;
          case "onBye":
            Util.onByeMessage(data);
            break;
          case "onLeave":
            hvuex({ userList: RTCClient.instance.getUserList() });
            break;
        }
      });
    },
    /**
     * 退出教室
     */
    exitRoom() {
      if (this.$store.state.data.role == 1) {
        Util.exitRoom();
      } else if (this.$store.state.data.role == 0) {
        this.exitRoomToast = true;
      } else {
        this.$router.push("/");
      }
    },
    /**
     * 显示错误信息
     */
    showToast() {
      if (this.$store.state.data.isInRTC) {
        Util.toast("未开始上课");
      } else {
        Util.toast("连麦后开启此功能");
      }
    },
    // 分享
    shareUrlCopy() {
      if (Util.inputCopy("share")) {
        Util.toast("链接复制成功");
      } else {
        Util.toast("链接复制失败");
      }
    }
  }
};
</script>
<style lang="scss">
.hfooter {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: vh(104);
  width: vw(1024);
  background-color: #f8f8f8;
  box-sizing: border-box;
  padding: vh(17) vw(32);
  .visiable{
    visibility: hidden;
  }
  #share {
    position: fixed;
    bottom: vh(-1111);
  }
  .function {
    margin-left: vw(138);
    display: flex;
    .vsersion {
      position: absolute;
      left: vw(10);
    }
    :hover {
      i {
        background-color: #bfbfbf;
      }
    }
    .on {
      i {
        background-color: #333333;
      }
    }
    .initstate {
      i {
        background-color: #013ebe;
      }
      i:hover {
        background-color: #0036a6;
      }
    }
    div {
      text-align: center;
      margin-right: vw(24);
      cursor: pointer;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      i {
        width: vw(100);
        height: vh(48);
        border-radius: vh(24);
        border-radius: 24px;
        background-color: #e5e5e5;
        margin-bottom: 12px;
        cursor: pointer;
        background-repeat: no-repeat;
        background-position: center center;
        background-size: vh(40) vh(40);
      }
      span {
        font-size: vh(16);
        line-height: vh(22);
        background-color: #f8f8f8;
        color: #2f2f2f;
      }
    }
    .startclass p {
      background-color: #013ebe;
      &:hover {
        background-color: #0036a6;
      }
    }
    .leaveclass {
      i {
        width: vh(48);
        height: vh(48);
        border-radius: 50%;
      }
    }
    .footer-setting {
      position: absolute;
      right: 0;
    }
  }
  .teacher {
    margin-left: vw(178);
  }
}
</style>