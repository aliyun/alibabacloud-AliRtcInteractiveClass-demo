<template>
  <div class="hui-modal">
    <div class="modal-header">
      <span @click="cancel">x</span>
    </div>
    <h3>您确定要离开教室吗？</h3>
    <h4>结束课程后无法再次回到同一个教室，需要老师重新创建</h4>
    <div class="modal-footer">
      <div @click="exitRoom" class="leave-submit">确定</div>
      <div @click="cancel" class="leave-cancel">取消</div>
    </div>
  </div>
</template>

<script>
import Util from '../core/utils/utils';
import http from '../core/http/http';
export default {
  data() {
    return {};
  },
  name: "app",
  components: {},
  created() {},
  methods:{
    exitRoom() {
      http.stopMPUTask(this.$store.state.data.classNum).then(re=>{
         Util.exitRoom();
      })
    },
    cancel() {
      this.$parent.exitRoomToast = false;
    }
  }
};
</script>

<style lang="scss">
.hui-modal {
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: vh(197);
  width: vh(360);
  background: #ffffff;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.21);
  background-color: #ffffff;
  padding:vh(25) vw(25);
  .modal-header {
    height: vh(38);
    overflow: hidden;
    span {
      float: right;
      color: #bfbfbf;
      font-size: vh(30);
      cursor: pointer;
    }
  }
  > h3 {
    color: #111111;
    font-size: vh(18);
  }
  > h4 {
    margin-top:vh(10);
    font-size: vh(14);
    line-height:1.3;
    color: #888888;
  }
  .modal-footer{
    display: flex;
    justify-content: flex-end;
    margin-top: vh(20);
    div{
      border: 1px solid #013EBE;
      width: vh(80);
      height: vh(30);
      border-radius: vh(15);
      text-align: center;
      line-height: vh(30);
      color: #013EBE;
      cursor: pointer;
    }
    .leave-submit{
      background: #013EBE;
      color: #ffffff;
      margin-right: vw(10);
    }
  }
}
</style>
