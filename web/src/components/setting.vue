<template>
  <div class="setting">
    <p class="title">高级设置</p>
    <video autoplay class="video mirrorMode" id="testVideo"></video>
    <div class="hform-div">
      <span>摄像头</span>
      <select class="hform-div-input" v-model="currentCamera" @change="switchCamera">
        <option v-for="(v,i) in videoDevices" :key="i" :value="v.deviceId">{{v.label}}</option>
      </select>
    </div>
    <div class="hform-div">
      <span>麦克风</span>
      <select class="hform-div-input" v-model="currentAudioCapture">
        <option v-for="(v,i) in audioDevices" :key="i" :value="v.deviceId">{{v.label}}</option>
      </select>
    </div>
    <div class="mic-show">
      <i class="iconfont icon-maikefeng"></i>
      <meter high="0.25" max="1" :value="level"></meter>
    </div>
    <button class="submit-button" type="button" @click="submit">完成设置</button>
  </div>
</template>
<script>
import RTCClient from "../core/rtc-client";
export default {
  data() {
    return {
      level: 0,
      currentCamera: null,
      currentAudioCapture: null,
      audioDevices: [],
      videoDevices: [],
      instance: new WebrtcCheck()
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.getDevices();
    });
  },
  methods: {
    startPreview() {
      RTCClient.instance.startPreview(document.getElementById("testVideo"));
    },
    getDevices() {
      RTCClient.instance
        .getDevices()
        .then(re => {
          this.audioDevices = re.audioDevices;
          this.videoDevices = re.videoDevices;
          try {
            this.currentAudioCapture = this.audioDevices[0].deviceId;
            this.testMic();
          } catch (error) {
            
          }
          this.currentCamera = this.videoDevices[0].deviceId;
          RTCClient.instance.currentCamera(this.videoDevices[0].deviceId);
          if (!this.$store.state.data.isInRTC) {
            this.startPreview();
          }else{
          }
        });
    },
    testMic() {
      this.instance.startAudioInputLevel(re => {
        this.level = re;
      });
    },
    /**
     * 切换摄像头
     */
    switchCamera(deviceId) {
      if (this.$store.state.data.isInRTC) {
        RTCClient.instance.currentCamera(this.currentCamera);
      } else {
        RTCClient.instance
          .stopPreview()
          .then(re => {
            RTCClient.instance.currentCamera(this.currentCamera);
            this.startPreview();
          });
      }
    },
    /**
     * 完成设置
     */
    submit() {
      this.instance.stopAudioInputLevel();
      if (!this.$store.state.data.isInRTC) {
        RTCClient.instance
          .stopPreview()
          .then(re => {
            RTCClient.instance.currentCamera(this.currentCamera);
            RTCClient.instance.currentAudioCapture(
              this.currentAudioCapture
            );
            hvuex({ isShowSetting: "" });
          });
      } else {
        RTCClient.instance.currentCamera(this.currentCamera);
        RTCClient.instance.currentAudioCapture(this.currentAudioCapture);
        hvuex({ isShowSetting: "" });
      }
    }
  }
};
</script>
<style lang="scss">
.setting {
  z-index: 100;
  position: fixed;
  height: 100vh;
  width: vw(515);
  right: 0;
  top: 0;
  box-sizing: border-box;
  padding: vw(40) vw(155) vw(40) vw(58);
  background-color: white;
  box-shadow: vw(-5) 0 vw(5) rgba(136, 136, 136, 0.5);
  .title {
    font-size: vh(24);
    line-height: vh(33);
    color: #181818;
    font-weight: 800;
  }
  .video {
    margin: vh(12) 0 0;
    background-color: #d8d8d8;
    height: vh(163);
    // width: vh(302);
  }
  .hform-div {
    margin-top: vh(32);
    width: vh(302);
    border-bottom: vw(1) solid $borderColor;
    span {
      font-size: vh(12);
      line-height: vh(17);
      color: #888888;
    }
    .hform-div-input {
       appearance:none;
      -moz-appearance:none;
      -webkit-appearance:none;
      outline: none;
      height: vh(32);
      font-size: vh(22);
      color: #181818;
      border: 0;
      background-color: transparent;
      background: url('../assets/img/jiantouxia.png') no-repeat right 5px center;
      background-size: 20px 20px;
      padding-right: vh(45);
      width: 100%;
    }
  }
  .mic-show {
    margin: vh(30) 0;
    .icon-maikefeng {
      font-size: vh(30);
      vertical-align: middle;
    }
    meter {
      width: vh(280);
    }
  }
  .submit-button {
    margin-top: vh(40);
    display: block;
    width: vh(302);
    height: vh(48);
    text-align: center;
    line-height: vh(48);
    border-radius: vh(24);
    color: white;
    font-size: vh(23);
    background-color: rgb(10, 67, 187);
    letter-spacing: vh(1);
    border:0;
    outline: none;
    cursor: pointer;
  }
}
</style>