import http from "./http/http";
import store from '../vuex/state';
import Util from "./utils/utils";
export default class AliPlayClient {
  constructor() {
    this._client = null;
  }
  static get instance() {
    if (!AliPlayClient._intance) {
      AliPlayClient._intance = new AliPlayClient();
    }
    return AliPlayClient._intance;
  }
  static palyVideo(channelId) {
    http.getPlayUrl(channelId).then(re => {
      document.getElementById("player-con") ? document.getElementById("player-con").remove() : '';
      let element = document.createElement("div");
      element.setAttribute("id", "player-con");
      document.getElementById("student-box").appendChild(element);
      let playerUrl=re.data.playUrl.flv;
      if(Util.isMobile()){
        playerUrl=re.data.playUrl.hls;
      }
      window.player = new Aliplayer({
        "id": "player-con",
        "source": playerUrl,
        "width": "100%",
        "height": "100%",
        "autoplay": true,
        "isLive": true,
        "rePlay": false,
        "loadDataTimeout":"30",
        "waitingTimeout":"30",
        "playsinline": true,
        "preload": true,
        "cover":require('../assets/img/学生-教师的课程尚未开始.png'),
        "controlBarVisibility": "click",
        "showBarTime": 5000,
        "useH5Prism": true,
        "skinLayout": [
          {
            "name": "H5Loading",
            "align": "cc"
          },
          {
            "name": "errorDisplay",
            "align": "tlabs",
            "x": 0,
            "y": 0
          },
          {
            "name": "infoDisplay"
          },
          {
            "name": "bigPlayButton",
            "align": "blabs",
            "x": 30,
            "y": 80
          },
          {
            "name": "tooltip",
            "align": "blabs",
            "x": 0,
            "y": 56
          },
          {
            "name": "thumbnail"
          },
          {
            "name": "controlBar",
            "align": "blabs",
            "x": 0,
            "y": 0,
            "children": []
          }
        ]
      }, function (re) {
        player.play();
        console.log("The player is created");
      });
      player.on('play',function(data){
        console.log('play');
      });
      player.on('error',function(data){
       console.error(data);
       if(data&&data.paramData&&data.paramData.error_code==4006){
         hv.$alert("老师已离开", "", {
           confirmButtonText: '确定',
           callback: action => {
              hv.$router.push("/");
           }
         });
       }
      });
    })
  }

}