<template>
  <div class="studentOnly">
    <hheader v-if="!isMobile"></hheader>
    <div class="student-box" id="student-box" :class="{'rotate':isMobile}">
      <div id="player-con"></div>
    </div>
  </div>
</template>
<script>
import AliPlayClient from "../../core/aliplay-client";
import Util from "../../core/utils/utils";
export default {
  data() {
    return {
      isMobile:Util.isMobile(),
      isShowIcon:Util.isMobile()
    };
  },
  created() {
    
  },
  mounted() {
    this.$nextTick(() => {
      let channel = Util.getUrlParam("channel");
      hvuex({ classNum: channel });
      AliPlayClient.palyVideo(channel);
    });
  },
  methods:{
    toPlay(){
      this.isShowIcon=false;
      player.play();
    }
  }
};
</script>

<style lang="scss">
.studentOnly {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  .student-box {
    height: vh(720-40);
  }
  .rotate {
    position: absolute;
    width: 100vh;
    height: 100vw;
    transform: rotate(90deg);
    transform-origin: left center;
    left: 50vw;
    top: -28vh;
  }
  .icon-bofang{
    position: fixed;
    display: block;
    left:10px;
    bottom:10px;
    z-index: 100;
    color: white;
    font-size: vh(70);
  }
}
</style>