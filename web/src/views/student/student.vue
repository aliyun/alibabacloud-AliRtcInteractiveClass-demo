<template>
  <div class="student">
    <hheader></hheader>
    <div class="student-box" id="student-box">
      <div class="video-div">
        <video
          autoplay
          v-show="$store.state.data.isInRTC"
          :class="{'mirrorMode':mirrorModeState}"
          poster="../../assets/img/教师-学生的摄像头已关闭.png"
          id="localVideo"
        ></video>
      </div>
      <div id="player-con" class="player-con" v-show="!$store.state.data.isInRTC"></div>
      <div class="user-div" :class="{'on':$store.state.data.isInRTC}">
        <userlist v-show="$store.state.data.isInRTC"></userlist>
      </div>
    </div>
    <hfooter></hfooter>
  </div>
</template>
<script>
import RTCClient from "../../core/rtc-client";
import AliPlayClient from "../../core/aliplay-client";
import Util from "../../core/utils/utils";
export default {
  data() {
    return {};
  },
  created() {
    hvuex({ isSwitchScreen: true,isInRTC:false });
  },
  mounted() {
    this.$nextTick(() => {
      this.startLiveStreaming();
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
     * 直播拉流
     */
    startLiveStreaming() {
      AliPlayClient.palyVideo(this.$store.state.data.classNum);
    },
  }
};
</script>

<style lang="scss">
.student {
  .student-box {
    width: 100%;
    display: flex;
    height: vh(720-40-104);
    .video-div {
      flex: 1;
      height: vh(576);
      background-color: black;
      video {
        height: 100%;
        width: 100%;
      }
    }
    .on {
      width: 206px;
    }
  }
}
</style>