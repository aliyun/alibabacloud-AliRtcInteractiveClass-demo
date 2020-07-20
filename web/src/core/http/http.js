import axios from 'axios';
import config from '../data/config'
export default {
    /**
     * 获取authinfo
     * @param {*} channel
     * @param {*} userName
     */
    getAuthInfo(channel, userName) {
        return new Promise((resolve, reject) => {
            let url = `${config.baseUrl}/getRtcAuth?channelId=${channel}`;
            axios.get(url).then(re => {
                re.data.data.channel = channel;
                re.data.data.userName = userName;
                resolve(re.data.data);
            }).catch(err => {
                console.error(`channel:${channel}`, err);
                reject(err);
                alert(err);
            })
        })
    },
    /**
    * 获取播放地址
    * @param {*} channel 
    */
    getPlayUrl(channel) {
        return new Promise((resolve, reject) => {
            let url = `${config.baseUrl}/getPlayUrl?channelId=${channel}&userId=""&role=""`;
            axios.get(url, {}).then(re => {
                resolve(re.data);
            }).catch(err => {
                reject(err);
            })
        })
    },
    /**
     * 开启旁路直播
     * @param {*} channel 
     * @param {*} userId 
     */
    startMPUTask(channel, userId) {
        return new Promise((resolve, reject) => {
            let url = `${config.baseUrl}/startMPUTask?channelId=${channel}&userId=${userId}&role=""`;
            axios.get(url, {}).then(re => {
                resolve(re.data);
            }).catch(err => {
                reject(err);
            })
        })
    },
    /**
     * 更新旁路直播
     * @param {*} channel 
     * @param {*} userId
     * @param {*} sourceType 
     */
    async updateMPUTask(channel, userId,sourceType) {
        let url = `${config.baseUrl}/updateMPULayout?channelId=${channel}&userId=${userId}&role=""&sourceType=${sourceType}`;
        return axios.get(url, {})
    },
    /**
    * 停止旁路直播
    * @param {*} channel 
    */
    async stopMPUTask(channel) {
        let url = `${config.baseUrl}/stopMPUTask?channelId=${channel}&userId=""&role=""`;
        return axios.get(url, {})
    },
    /**
     * 
     * @param {*} channelId 
     */
    describeChannelUsers(channelId) {
        return new Promise((resolve, reject) => {
            let url = `${config.baseUrl}/describeChannelUsers?channelId=${channelId}`;
            axios.get(url, {}).then(re => {
                resolve(re.data);
            }).catch(err => {
                reject(err);
            })
        })
    }
}