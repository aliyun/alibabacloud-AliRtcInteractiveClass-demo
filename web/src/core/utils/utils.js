import { Message } from 'element-ui';
import RTCClient from "../rtc-client";
import http from '../http/http';
import state from '../../vuex/state';
export default class Util {
    /**
     * 显示信息
     * @param {*} v 
     */
    static toast(v) {
        Message(v);
    }
    /**
     * 复制
     * @param {*} id 
     */
    static hCopy(id) {
        console.log(id);
        if (id) {
            try {
                var range = document.createRange();
                var tar = document.getElementById(id);
                range.selectNodeContents(tar);
                var selection = window.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
                document.execCommand('copy');
                selection.removeAllRanges();
            } catch (error) {
                console.log(error);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }
    /**
     * 
     * @param {*} id 
     */
    static inputCopy(id) {
        try {
            var Url2 = document.getElementById(id);
            Url2.select(); // 选择对象
            document.execCommand("Copy");
            return true;
        } catch (error) {
            console.error(error);
            return false;
        }
    }
    /**
  * 获取浏览器地址栏参数
  * @param {*} url 
  * @param {*} name 
  */
    static getUrlParam(name) {
        let url = window.location.href;
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
        let urlArr = url.split("?");
        if (urlArr.length > 1) {
            url = "?" + urlArr[1];
            var r = url.substr(1).match(reg);
            if (r != null) return decodeURIComponent(r[2]); return null;
        } else {
            return null;
        }
    }
    /**
     * 
     */
    static isMobile() {
        var userAgentInfo = navigator.userAgent;
        var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
        var flag = false;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = true; break; }
        }
        return flag;
    }
    /**
     * 退出房间
     */
    static exitRoom() {
        RTCClient.instance
            .leaveChannel()
            .finally(() => {
                hvuex({ isPublishScreen: false, isPublish: false, isPreview: RTCClient.instance.isPreview });
                hv.$router.push("/");
            });
    }
    /**
     * 开始预览
     */
    static startPreview() {
        RTCClient.instance
            .startPreview(document.getElementById("localVideo"))
            .then(() => {
                AppConfig.localStream=document.getElementById("localVideo").srcObject;
                hvuex({ isPreview: true });
            })
            .catch(err => {
                console.error("", err);
            });
    }
    /**
     * 显示远端用户
     * @param {*} data 
     */
    static showRemoteVideo(data) {
        let video = document.getElementById(data.userId);
        let subUserId = document.getElementById("localVideo").getAttribute("subUserId");
        if (subUserId && subUserId == data.userId) {
            video = document.getElementById("localVideo");
        }
        RTCClient.instance.setDisplayRemoteVideo(data.userId, video, data.code);
        // if (subUserId && subUserId == data.userId) {
        //     setTimeout(() => {
        //         document.getElementById(data.userId).srcObject = document.getElementById("localVideo").srcObject;
        //     }, 100);
        // }
    }
    /**
     * 
     * @param {*} code 
     */
    static onByeMessage(code) {
        console.log(code);
        let messageTxt = "";
        if (code == 1) {
            messageTxt = "10分钟体验时间已到";
        } else if (code == 2) {
            messageTxt = "10分钟体验时间已到";
        } else {
            messageTxt = "同一个用户ID在其他端登录";
        }
        hv.$alert(messageTxt, "", {
            confirmButtonText: '确定',
            callback: action => {
                hv.$router.push("/");
            }
        });
    }
    /**
     * 显示错误
     */
    static showErrorMsg(data) {
        let resmsg = "";
        switch (data.errorCode) {
            case 10000:
                resmsg += "设备未知错误";
                break;
            case 10001:
                resmsg += "未找到音频设备";
                break;
            case 10002:
                resmsg += "未找到视频设备";
                break;
            case 10003:
                resmsg += "浏览器禁用音频设备";
                break;
            case 10004:
                resmsg += "浏览器禁用视频设备";
                break;
            case 10005:
                resmsg += "系统禁用音频设备";
                break;
            case 10006:
                resmsg += "系统禁用视频设备";
                break;
            case 10010:
                resmsg += "屏幕共享未知错误";
                break;
            case 10011:
                {
                    resmsg += "屏幕共享被禁用";
                    hvuex({ isPublishScreen: false });
                    http.updateMPUTask(RTCClient.instance.channel, RTCClient.instance.userId, "camera");
                }
                break;
            case 10012:
                resmsg += "屏幕共享已取消";
                hvuex({ isPublishScreen: false });
                http.updateMPUTask(RTCClient.instance.channel, RTCClient.instance.userId, "camera");
                if(state.data.isSwitchScreen){
                    if(state.data.isPublishScreen){
                        document.getElementById(RTCClient.instance.userId).srcObject = RTCClient.instance.screenStream;
                    }else{
                        document.getElementById(RTCClient.instance.userId).srcObject = AppConfig.localStream;
                    }
                }else{
                    if(state.data.isPublishScreen){
                        document.getElementById("localVideo").srcObject = RTCClient.instance.screenStream;
                    }else{
                        document.getElementById("localVideo").srcObject = AppConfig.localStream;
                    }
                }
                if(document.getElementById(RTCClient.instance.userId).srcObject == AppConfig.localStream){
                   document.getElementById(RTCClient.instance.userId).srcObject = null;
                }
                break;
            case 10201:
                resmsg += "自动播放失败";
                break;
            case 10300:
                resmsg += "直播拉流失败，请重新拉流";
                document.getElementById("localVideo").srcObject = null;
                break;
            default:
                break;
        }
        resmsg ? Util.toast(resmsg) : Util.toast(data);
    }
}
