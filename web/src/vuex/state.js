export default {
  //所有
  data: {
    version:"2.0.12",
    classNum: "",// 教室号
    userName: '',// 用户名
    userId:null,// userId
    role: null,// 角色
    client: null,// rtc 实例
    isShowSetting: false,// 是否显示配置项
    isInRTC: false,
    isPublish:false,
    isPreview:false,
    isPublishScreen:false, //屏幕分享
    isSwitchScreen:false,// 是否切换大小流
    userList:[],
    supportInfo:{}
  },
  //网站基础数据
  config: {

  }
}
