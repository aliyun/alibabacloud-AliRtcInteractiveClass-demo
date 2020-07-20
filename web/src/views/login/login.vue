<template>
  <div class="login" @click="hideSetting">
    <header class="login-header">
      <i class="iconfont icon-rtcyinshipintongxin"></i>
      音视频通信 {{$store.state.data.version}}
      <i
        class="iconfont icon-shezhi"
        @click.stop="showSetting"
      ></i>
    </header>
    <div class="login-card">
      <div class="login-card-left">
        <img src="../../assets/img/低延时大课班.png" alt srcset />
      </div>
      <div class="login-card-right">
        <p class="title">互动直播课</p>
        <div class="button-group">
          <button class="button" :class="role==0?'on':''" @click="swicthRole(0)">教师</button>
          <button class="button" :class="role==1?'on':''" @click="swicthRole(1)">学生</button>
        </div>
        <div class="form">
          <span class="form-label" :class="userNameFocus || userName ? 'form-label-show' : ''">姓名</span>
          <input
            type="text"
            class="form-input"
            v-model="userName"
            :placeholder="userNamePlaceHolder"
            @focus="changeLoginFocus(0, true)"
            @blur="changeLoginFocus(0, false)"
          />
          <span class="form-label" :class="classNumFocus || classNum ? 'form-label-show' : ''">教室码</span>
          <classnumber
            :codeLength="6"
            class="form-input-class"
            placeholder="教室码（6位数字）"
            v-model="classNum"
            @changeLoginFocus="changeLoginFocus"
          ></classnumber>
        </div>
        <button
          class="submit-button"
          :class="(!classNum||classNum.length!=6||!userName)?'disable':''"
          :disabled="!classNum||classNum.length!=6||!userName"
          type="submit"
          @click="onSubmit"
        >开始上课</button>
      </div>
    </div>
  </div>
</template>

<script>
import RTCClient from "../../core/rtc-client";
import Util from "../../core/utils/utils";
import http from "../../core/http/http";
export default {
  data() {
    return {
      role: 0,
      userName: "",
      classNum: null,
      userNameFocus: false,
      classNumFocus: false,
      userNamePlaceHolder: "您的姓名"
    };
  },
  created() {
    RTCClient.instance.isSuppert().then(re=>{
        hvuex({supportInfo:re})
        RTCClient.instance.getDevices().then(re=>{
          try {
             RTCClient.instance.currentAudioCapture(re.audioDevices[0].deviceId);
             RTCClient.instance.currentCamera(re.videoDevices[0].deviceId);
          } catch (error) {
            
          }
        })
    });
    hvuex({ role: this.role });
  },
  mounted() {
    this.$nextTick(()=>{
      
    })
  },
  methods: {
    /**
     * 登陆
     */
    onSubmit() {
      if (!this.userName) {
        Util.toast("用户名不能为空");
        return;
      }
      if (!this.classNum || this.classNum.length != 6) {
        console.log(this.classNum.length);
        Util.toast("教室码不正确");
        return;
      }
      hvuex({
        role: this.role,
        classNum: this.classNum,
        userName: this.userName
      });
      if (this.role == 0) {
        RTCClient.instance.setClientRole(0);
        this.$router.push("/teacher");
      } else {
        http.describeChannelUsers(this.classNum).then(re => {
          if(re.data.commTotalNum==0){
            this.$alert("教室码不存在，若未创建教室，请先从电脑端创建", "", {
             confirmButtonText: '确定',
             callback: action => {}
            });
            return false;  
          }
          RTCClient.instance.setClientRole(1);
          this.$router.push("/student");
        });
      }
    },
    /**
     * 显示配置页
     */
    showSetting() {
      hvuex({ isInRTC: false, isShowSetting: "setting" });
    },
    /**
     * 显示配置页
     */
    hideSetting() {
      hvuex({ isShowSetting: "" });
    },
    swicthRole(n) {
      this.role = n;
      hvuex({ role: n });
    },
    changeLoginFocus(n, flag) {
      if (n === 0) {
        this.userNameFocus = flag;
        this.userNamePlaceHolder = flag ? "" : "您的姓名";
      } else {
        this.classNumFocus = flag;
      }
    }
  },
  watch: {
    classNum() {}
  }
};
</script>

<style lang="scss">
.login {
  height: 100vh;
  overflow: hidden;
  position: relative;
  .login-header {
    margin: vh(32) vw(47) 0;
    font-size: vh(20);
    .icon-rtcyinshipintongxin {
      font-size: vh(40);
      vertical-align: middle;
      margin-right: vw(5);
    }
    .icon-shezhi {
      font-size: vh(31);
      cursor: pointer;
      float: right;
    }
  }
  .login-card {
    margin: vh(91) vw(110) 0;
    display: flex;
    .login-card-left {
      width: vw(402);
      img {
        width: 80%;
        height: auto;
        top: vh(-80);
        position: relative;
      }
    }
    .login-card-right {
      flex: 1;
      .title {
        font-size: vh(45);
        line-height: vh(62);
        letter-spacing: vw(2);
      }
      .button-group {
        margin-top: vh(48);
        .button {
          width: vw(153);
          height: vh(48);
          background: #f6f9ff;
          border: vh(2) solid #c1c1c1;
          background-color: #ffffff;
          cursor: pointer;
          font-size: vh(18);
          color: #555555;
          outline: none;
          position: relative;
        }
        .button:nth-child(1) {
          border-radius: vh(24) 0 0 vh(24);
        }
        .button:nth-child(2) {
          border-radius: 0 vh(24) vh(24) 0;
          transform: translateX(vh(-2));
        }
        .on {
          border: vh(2) solid #013ebe;
          background-color: #f6f9ff;
          color: #013ebe;
          z-index: 1;
        }
      }
      .form {
        .form-label {
          display: inline-block;
          margin-top: vh(18);
          font-size: 12px;
          opacity: 0;
          color: #888888;
        }
        .form-label-show {
          opacity: 1;
        }
        .form-input {
          outline: none;
          font-size: vh(22);
          display: block;
          border-bottom: vh(1) solid $borderColor;
          color: #181818;
          padding: vh(10) 0;
          width: vw(306);
          // margin-bottom: vh(25);
        }
        .form-input-class {
          // margin-top: vh(25);
          width: vw(306);
        }
        input::-webkit-input-placeholder {
          color: #888888;
        }
      }
      .submit-button {
        margin-top: vh(30);
        display: block;
        width: vw(306);
        height: vh(48);
        text-align: center;
        line-height: vh(48);
        border-radius: vh(24);
        color: white;
        font-size: vh(23);
        background-color: rgb(10, 67, 187);
        letter-spacing: vh(1);
        outline: none;
        cursor: pointer;
        border: 0;
      }
      .disable {
        background-color: grey;
      }
    }
  }
}
</style>