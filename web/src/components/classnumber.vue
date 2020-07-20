<template>
  <div class="classnumber">
    <div class="v-code">
      <input
        ref="vcode"
        id="vcode"
        type="tel"
        maxlength="codeLength"
        v-model="aCodes"
        :disabled="$store.state.data.role==0"
        @focus="focused = true"
        @blur="focused = false"
        :placeholder="placeholder"
        :class="{'on':focused||aCodes}"
        autocomplete="off"
      />
      <label
        v-show="focused||aCodes"
        for="vcode"
        class="line"
        v-for="(item,index) in codeLength"
        :key="index"
        :class="{'animated': focused && cursorIndex === index}"
        v-text="codeArr[index]"
      ></label>
    </div>
  </div>
</template>
<script>
import Util from '../core/utils/utils';
export default {
  props: {
    codeLength: {
      type: Number,
      default: 4
    },
    placeholder: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      aCodes: "",
      telDisabled: false,
      focused: false
    };
  },
  created(){
    this.setInit();
  },
  model: {
    prop: "value", //绑定的值，通过父组件传递
    event: "update2" //自定义名 可以随便改
  },
  methods:{
    setInit(){
       let numClass=parseInt(Math.random()*1000000).toString();
       if(numClass.length==6){
         this.aCodes=numClass;
       }else{
         this.setInit();
       }
    }
  },
  computed: {
    codeArr() {
      return this.aCodes.split("");
    },
    cursorIndex() {
      return this.aCodes.length;
    }
  },
  watch: {
    aCodes(newVal, oldVal) {
      if (newVal.replace(/[^\d]/g, "") != newVal||(newVal && newVal.length > this.codeLength)) {
        this.aCodes = oldVal;
        return false;
      }
      this.$emit("update2", newVal);
    },
    "$store.state.data.role"(newVal, oldVal){
       if(newVal==0){
          this.setInit();
       }else{
         this.aCodes="";
         if(Util.getUrlParam("channel")&&Util.getUrlParam("role")==1){
            this.aCodes=Util.getUrlParam("channel");
         }
       }
    },
    focused(newVal, oldVal) {
      this.$emit("changeLoginFocus", 1, newVal)
    }
  }
};
</script>
<style lang="scss">
.v-code {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  position: relative;
  width: 100%;
  margin-left: auto;
  margin-right: auto;
  padding: vh(10) 0;
}

.v-code input {
  position: relative;
  border-bottom: vh(1) solid $borderColor;
  color: #181818;
  height: 32px;
  line-height: 32px;
  outline: none;
  display: block;
  width: 100%;
  font-size: vh(22);
}
.v-code .on {
  opacity: 0;
  position: absolute;
}
.v-code .line {
  position: relative;
  width: 50px;
  height: 32px;
  line-height: 32px;
  text-align: center;
  color: #181818;
  font-size: vh(20);
  line-height: vh(28);
}
.v-code .line::after {
  display: block;
  position: absolute;
  content: "";
  left: 0;
  width: 100%;
  bottom: 0;
  height: vh(1);
  background-color: #aaaaaa;
  transform: scaleY(0.5);
  transform-origin: 0 100%;
}
.v-code .line.animated::before {
  display: block;
  position: absolute;
  left: 50%;
  top: 20%;
  width: 1px;
  height: 60%;
  content: "";
  background-color: #333333;
  animation-name: coruscate;
  animation-duration: 1s;
  animation-iteration-count: infinite;
  animation-fill-mode: both;
}

@keyframes coruscate {
  0% {
    opacity: 0;
  }
  25% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  75% {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
</style>