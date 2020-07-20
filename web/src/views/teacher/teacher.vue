<template>
  <div class="teacher">
    <hheader></hheader>
    <div class="teacher-box">
      <div class="video-div">
        <video
          autoplay
          :class="{'mirrorMode':mirrorModeState}"
          poster="../../assets/img/教师-学生的摄像头已关闭.png"
          id="localVideo"
        ></video>
      </div>
      <div class="user-div">
        <userlist></userlist>
      </div>
    </div>
    <hfooter></hfooter>
  </div>
</template>
<script>
import RTCClient from "../../core/rtc-client";
import Util from "../../core/utils/utils";
import http from "../../core/http/http";
export default {
  data() {
    return {};
  },
  created() {
    hvuex({ isSwitchScreen: false });
    this.registerCallBack();
  },
  mounted() {
    this.$nextTick(() => {
      RTCClient.instance
        .joinChannel()
        .then(userId => {
          Util.startPreview();
          http.startMPUTask(this.$store.state.data.classNum, RTCClient.instance.userId)
          hvuex({
            isInRTC: true,
            isPublish: true,
            userId: userId,
            isPreview: true
          });
        })
        .catch(err => {
        });
    });
  },
  computed: {
    mirrorModeState() {
      if (this.$store.state.data.isSwitchScreen) {
        return false;
      } else {
        if (!this.$store.state.data.isPreview) {
          return false;
        }
      }
      return true;
    }
  },
  methods: {
    /**
     * 注册回调
     */
    registerCallBack() {
      RTCClient.instance.registerCallBack((eventName, data) => {
        switch (eventName) {
          case "onJoin":
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
          case "onNotify":
            Util.updateImgState(data);
            break;
          case "onLeave":
            hvuex({ userList: RTCClient.instance.getUserList() });
            break;
        }
      });
    }    
  }
};
</script>

<style lang="scss">
.teacher {
  .teacher-box {
    width: 100%;
    display: flex;
    .video-div {
      flex: 1;
      height: vh(576);
      video {
        height: 100%;
        width: 100%;
        background-color: black;
      }
    }
  }
}
</style>