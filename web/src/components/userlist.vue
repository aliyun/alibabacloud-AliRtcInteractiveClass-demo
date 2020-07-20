<template>
  <div class="user-list">
    <div class="title">
      <p :class="{'on':showModel==0}" @click="showModel=0">互动画面</p>
      <p :class="{'on':showModel==1}" @click="showModel=1">互动列表({{$store.state.data.userList.length+1}})</p>
    </div>
    <ul class="list-video" v-show="showModel==0">
      <li>
        <div class="avatar">
          <img src="../assets/img/教师-学生的摄像头已关闭.png" alt srcset />
        </div>
        <video
          autoplay
          class="mirrorMode myUserId"
          :id="$store.state.data.userId"
          @click="switchScreen(myUserId)"
        ></video>
        <p class="name">{{$store.state.data.userName}}</p>
      </li>
      <li v-for="(v,i) in $store.state.data.userList" :key="i">
        <div class="avatar">
          <img src="../assets/img/教师-学生的摄像头已关闭.png" alt srcset />
        </div>
        <video
          autoplay
          :id="v.userId"
          poster="../assets/img/教师-学生的摄像头已关闭.png"
          @click="switchScreen(v.userId)" 
          v-if="$store.state.data.role==1"
        ></video>
         <video
          autoplay
          :id="v.userId"
          poster="../assets/img/教师-学生的摄像头已关闭.png"
          @click="switchScreen(v.userId)"
          v-else
        ></video>
        <p class="name">{{v.displayName}}</p>
      </li>
    </ul>
    <ul class="list-txt" v-show="showModel==1">
      <li>
        <p class="onlyname">{{$store.state.data.userName}}</p>
      </li>
      <li v-for="(v,i) in $store.state.data.userList" :key="i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
    </ul>
  </div>
</template>
<script>
import RTCClient from "../core/rtc-client";
import Util from '../core/utils/utils';
export default {
  data() {
    return {
      showModel: 1,
      myUserId: null,
      subUserId: null
    };
  },
  mounted() {
    this.$nextTick(()=>{

    })
  },
  methods: {
    switchScreen(userId){
      this.myUserId = RTCClient.instance.userId;
      this.$nextTick(() => {
        if (userId == this.myUserId || userId == this.subUserId) {
          document.getElementById("localVideo").srcObject=AppConfig.localStream;
          document.getElementById("localVideo").removeAttribute("subUserId");
          hvuex({ isSwitchScreen: false });
          this.subUserId = null;
        } else {
          let oldSubUserId = document.getElementById("localVideo").getAttribute("subUserId");
          RTCClient.instance.subscribeLarge(userId).then(code=>{
            let streamConfigs=RTCClient.instance.getUserInfo(userId).streamConfigs;
            let obj=streamConfigs.getObjByproprety("sophon_video_camera_large","label");
            let objScree=streamConfigs.getObjByproprety("sophon_video_screen_share","label");
            if(obj){
                if(obj.subscribed&& !objScree.subscribed){
                   if(obj.muted){
                     setTimeout(()=>{
                       document.getElementById("localVideo").srcObject=null;
                       document.getElementById(userId).srcObject=null;
                     },100);
                     return;
                   }
                }
            }
            RTCClient.instance.setDisplayRemoteVideo(userId,document.getElementById("localVideo"),code);
            document.getElementById(userId).srcObject=document.getElementById("localVideo").srcObject;
          })
          document.getElementById("localVideo").setAttribute("subUserId", userId);
          hvuex({ isSwitchScreen: true });
          this.subUserId = userId; 
        }
      });
    }
  },
  watch: {
    "$store.state.data.userList"(newVal, oldVal) {
      if(JSON.stringify(newVal)==JSON.stringify(oldVal)){
          return;
      }
      if (this.subUserId) {
        if (!RTCClient.instance.getUserInfo(this.subUserId)) {
          document.getElementById("localVideo").srcObject = document.getElementById(this.myUserId).srcObject;
          document.getElementById("localVideo").removeAttribute("subUserId");
          this.subUserId = null;
          hvuex({ isSwitchScreen: false });
        }
      }
    }
  }
};
</script>
<style lang="scss">
.user-list {
  //width: vw(218);
  width: 196px;
  height: vh(720-40-104);
  //position: fixed;
  top: 40px;
  right: 0;
  bottom: 104px;
  padding: 0 13px;
  box-sizing: border-box;
  .title {
    margin: 12px 0 12px 0;
    font-size: 16px;
    display: flex;
    text-align: center;
    cursor: pointer;
    p {
      position: relative;
      flex: 1;
      padding: 10px 0;
    }
    p::after {
      display: block;
      content: "";
      height: 1px;
      background-color: black;
      margin-top: 20px;
    }
    .on::after {
      height: 4px;
      background-color: #013ebe;
      margin-top: 18px;
    }
  }
  .list-video {
    overflow-x: scroll;
    height: vh(720-40-104-80);
    li {
      position: relative;
      $height: 126px; // 31.1vh;
      height: $height;
      overflow: hidden;
      margin-bottom: 10px;
      .avatar {
        position: absolute;
        text-align: center;
        top: 0;
        width: 100%;
        line-height: $height;
        bottom: 0;
        color: #333333;
        background-image: url("../assets/img/教师-学生的摄像头已关闭.png");
        background-position: center;
        background-size: contain;
        background-repeat: no-repeat;
        background-color: rgba(89, 89, 89, $alpha: 1);
        img {
          display: none;
        }
      }
      video {
        position: absolute;
        top: 0;
        width: 100%;
        height: $height;
        background-color: transparent;
      }
      .name {
        position: absolute;
        bottom: 10px;
        left: 10px;
        font-size: 18px;
        color: white;
      }
    }
  }
  .list-txt {
    p {
      line-height: 48px;
      cursor: pointer;
      text-indent: 30px;
    }
    p:hover {
      background-color: #d2e1ff;
    }
  }
}
</style>