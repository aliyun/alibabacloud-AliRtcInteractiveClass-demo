<template>
  <div class="nui-video-div" :id="userInfo.userId+'Div'" @click="switchScreen()">
    <!-- <img class="avatar" v-show="isMuteVideo()" src="../assets/img/教师-学生的摄像头已关闭.png" alt srcset /> -->
    <div class="avatar avatar-name" v-show="isMuteVideo()">
      {{userInfo.displayName}}
    </div>
    <video class="nui-video" :id="userInfo.userId"></video>
    <div class="nui-video-footer">
      <span class="user-name">{{userInfo.displayName}}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ["userInfo"],
  data() {
    return {
    };
  },
  methods: {
    isMuteVideo() {
      // console.error(this.userInfo);
      if (this.userInfo) {
        if (Array.isArray(this.userInfo.streamConfigs)&&this.userInfo.streamConfigs.length>0) {
          let index = this.userInfo.streamConfigs.findIndex((v) => {
            return v.label == "sophon_video_camera_large" && v.muted;
          });
          if (index > -1) {
            return true;
          }
        }else{
          return true;
        }
        if(this.$store.state.data.isSwitchScreen){
          if(document.getElementById("localVideo").getAttribute("subUserId")==this.userInfo.userId){
            return true;
          }
        }
      }
      return false;
    },
    switchScreen() {
      this.$emit("switchScreen", this.userInfo.userId);
    },
  },
};
</script>

<style lang="scss">
.nui-video-div {
  position: relative;
  height: vh(128);
  width: 100%;
  margin-bottom: 10px;
  .avatar {
    position: absolute;
    top: 0;
    left: 0;
    height: vh(128);
    width: 100%;
  }
  .avatar-name{
    color: white;
    font-size: vh(40);
    text-align: center;
    line-height: vh(110);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    background-color: #409eff;
  }
  .nui-video {
    width: 100%;
    height: 100%;
    // background-color: #409eff;
    object-fit: fill;
  }
  .nui-video-footer {
    position: absolute;
    bottom: vh(10);
    left: vh(10);
    width: 100%;
    .user-name {
      font-family: PingFangSC-Regular;
      font-size: vh(16);
      color: #ffffff;
      text-shadow: 0 vh(2) vh(4) rgba(0, 0, 0, 0.5);
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}
</style>