package com.alivc.liveclass.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alivc.base.AuthKeyUrlUtil;
import com.alivc.base.ConfigMapUtil;
import com.alivc.base.ResponseResult;
import com.alivc.base.RtcOpenAPI;
import com.aliyuncs.rtc.model.v20180111.DescribeChannelUsersResponse;
import com.aliyuncs.rtc.model.v20180111.StartMPUTaskResponse;
import com.aliyuncs.rtc.model.v20180111.StopMPUTaskRequest;
import com.aliyuncs.rtc.model.v20180111.StopMPUTaskResponse;
import com.aliyuncs.rtc.model.v20180111.UpdateMPULayoutRequest;
import com.aliyuncs.rtc.model.v20180111.UpdateMPULayoutResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class RtcauthController {

    @RequestMapping(value = "/getRtcAuth", method = RequestMethod.GET)
    public ResponseResult getRtcAuth(String platform, String channelId, String userId, String role) {

        log.debug(platform + "-" + channelId + "-" + userId + "-" + role);

        ResponseResult responseResult = new ResponseResult();

        userId = UUID.randomUUID().toString();

        try {
            JSONObject rtcToken = RtcOpenAPI.createToken(channelId, userId);

            responseResult.setData(rtcToken);

            String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

            ScheduledDeleteChannel.addChannel(appId, channelId);


            return responseResult;
        } catch (Exception e) {
            responseResult.setResult("false");
            responseResult.setCode("500");
            log.error("create RTC token error", e);
        }
        return responseResult;

    }

    @RequestMapping(value = "/startMPUTask", method = RequestMethod.GET)
    public ResponseResult onJoinSuccess(String platform, String channelId, String userId, String role) {

        String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

        String livePushDomain = ConfigMapUtil.getValueByKey("live_push_domain");
        String livePushDomainAuthKey = ConfigMapUtil.getValueByKey("live_push_domain_auth_key");

        String appStream = ConfigMapUtil.getValueByKey("live_app_stream_name");

        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = UUID.randomUUID().toString().replace("-", "");

        String pushUrl = "rtmp://" + livePushDomain + AuthKeyUrlUtil.getAuthedPath(appStream + channelId, livePushDomainAuthKey, timestamp, rand);


        log.debug(platform + "-" + channelId + "-" + userId + "-" + role);

        ResponseResult responseResult = new ResponseResult();


        try {
            StartMPUTaskResponse response = RtcOpenAPI.startMPUTask(channelId, pushUrl, channelId, appId, userId);

            return responseResult;
        } catch (Exception e) {
            responseResult.setResult("false");
            responseResult.setCode("500");
            log.error("create RTC token error", e);
        }
        return responseResult;

    }

    @RequestMapping(value = "/getPlayUrl", method = RequestMethod.GET)
    public ResponseResult getPlayUrl(String platform, String channelId, String userId, String role) {

        String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

        String livePushDomain = ConfigMapUtil.getValueByKey("live_push_domain");
        String livePushDomainAuthKey = ConfigMapUtil.getValueByKey("live_push_domain_auth_key");

        String livePlayDomain = ConfigMapUtil.getValueByKey("live_play_domain");
        String livePlayDomainAuthKey = ConfigMapUtil.getValueByKey("live_play_domain_auth_key");

        String liveArtpPlayDomain = ConfigMapUtil.getValueByKey("live_artp_play_domain");
        String liveArtpPlayDomainAuthKey = ConfigMapUtil.getValueByKey("live_artp_play_domain_auth_key");

        String appStream = ConfigMapUtil.getValueByKey("live_app_stream_name");

        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = UUID.randomUUID().toString().replace("-", "");

        String pushUrl = "rtmp://" + livePushDomain + AuthKeyUrlUtil.getAuthedPath(appStream + channelId, livePushDomainAuthKey, timestamp, rand);


        log.debug(platform + "-" + channelId + "-" + userId + "-" + role);

        ResponseResult responseResult = new ResponseResult();

        // 原画
        String playUrlArtp = "artp://" + liveArtpPlayDomain + AuthKeyUrlUtil.getAuthedPath(appStream + channelId, liveArtpPlayDomainAuthKey, timestamp, rand);
        String playUrlFlv = "https://" + livePlayDomain + AuthKeyUrlUtil.getAuthedPath(appStream + channelId + ".flv", livePlayDomainAuthKey, timestamp, rand);
        String playUrlHls1 = "https://" + livePlayDomain + AuthKeyUrlUtil.getAuthedPath(appStream + channelId + ".m3u8", livePlayDomainAuthKey, timestamp, rand);

        JSONObject playUrl = new JSONObject();
        playUrl.put("rts", playUrlArtp);
        playUrl.put("flv", playUrlFlv);
        playUrl.put("hls", playUrlHls1);

        JSONObject data = new JSONObject();
        data.put("playUrl", playUrl);

        responseResult.setData(data);

        return responseResult;

    }


    @RequestMapping(value = "/updateMPULayout", method = RequestMethod.GET)
    public ResponseResult uploadLayout(String platform, String channelId, String userId, String role, String sourceType) {
        UpdateMPULayoutRequest request = new UpdateMPULayoutRequest();

        String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

        request.setAppId(appId);

        request.setTaskId(channelId);

        List<UpdateMPULayoutRequest.UserPanes> userPanesList = new LinkedList<>();

        UpdateMPULayoutRequest.UserPanes userPanes = new UpdateMPULayoutRequest.UserPanes();
        userPanes.setUserId(userId);
        userPanes.setPaneId(0);
        userPanes.setSourceType(sourceType);

        userPanesList.add(userPanes);

        String layoutId = ConfigMapUtil.getValueByKey("layout");
        List layoutIdArr = new ArrayList();

        layoutIdArr.add(layoutId);

        request.setLayoutIdss(layoutIdArr);

        request.setUserPaness(userPanesList);

        log.debug(platform + "-" + channelId + "-" + userId + "-" + role);

        ResponseResult responseResult = new ResponseResult();

        try {
            UpdateMPULayoutResponse response = RtcOpenAPI.updateMPULayout(request);
            log.info(JSON.toJSONString(response));

            return responseResult;
        } catch (Exception e) {
            responseResult.setResult("false");
            responseResult.setCode("500");
            log.error("create RTC token error", e);
        }
        return responseResult;

    }


    @RequestMapping(value = "/stopMPUTask", method = RequestMethod.GET)
    public ResponseResult stopMPUTask(String platform, String channelId, String userId, String role) {
        StopMPUTaskRequest request = new StopMPUTaskRequest();

        String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

        request.setAppId(appId);
        request.setTaskId(channelId);

        log.debug(platform + "-" + channelId + "-" + userId + "-" + role);

        ResponseResult responseResult = new ResponseResult();

        try {

            StopMPUTaskResponse response = RtcOpenAPI.stopMPUTaskRequest(request);
            log.info(JSON.toJSONString(response));

            return responseResult;
        } catch (Exception e) {
            responseResult.setResult("false");
            responseResult.setCode("500");
            log.error("create RTC token error", e);
        }
        return responseResult;

    }

    @RequestMapping(value = "/describeChannelUsers", method = RequestMethod.GET)
    public ResponseResult describeChannelUsers(String channelId) {

        String appId = ConfigMapUtil.getValueByKey("rtc.liveclass.appId");

        ResponseResult responseResult = new ResponseResult();

        try {
            DescribeChannelUsersResponse response = RtcOpenAPI.describeChannelUsers(appId, channelId);
            responseResult.setData(response);
            return responseResult;
        } catch (Exception e) {
            responseResult.setResult("false");
            responseResult.setCode("500");
            log.error("describe Channel Users error", e);
        }
        return responseResult;

    }


}

