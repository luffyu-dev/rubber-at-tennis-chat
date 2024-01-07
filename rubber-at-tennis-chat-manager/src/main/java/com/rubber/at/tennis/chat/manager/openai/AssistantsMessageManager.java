package com.rubber.at.tennis.chat.manager.openai;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import com.rubber.at.tennis.chat.api.dto.message.ChatThreadMsgModel;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.utils.HttpProxyUtils;
import com.rubber.at.tennis.chat.manager.utils.OpenAiConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
@Slf4j
public class AssistantsMessageManager {


    /**
     * 添加消息详情
     * @param configDto
     * @param treadId
     * @param msg
     * @return
     *
     * {
     *     "id": "msg_aDFuwMBVV1fFoppfAuH4K0iu",
     *     "object": "thread.message",
     *     "created_at": 1704088641,
     *     "thread_id": "thread_zR7atctaC3yfbcQg3zvFPikV",
     *     "role": "user",
     *     "content": [
     *         {
     *             "type": "text",
     *             "text": {
     *                 "value": "微信小程序如何设置view的背景为图片",
     *                 "annotations": []
     *             }
     *         }
     *     ],
     *     "file_ids": [],
     *     "assistant_id": null,
     *     "run_id": null,
     *     "metadata": {}
     * }
     */
    public static ChatMessageDto addMessage(OpenAiConfigDto configDto,String treadId,String msg){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/messages";
        JSONObject body = new JSONObject();
        body.put("role","user");
        body.put("content",msg);

        String value = HttpProxyUtils.post(
                url,
                configDto.initHeader(),
                body,
                OpenAiConstant.DEFAULT_TIMEOUT);
        log.info("请求url={},value={}",url,value);

        JSONObject res = JSON.parseObject(value);
        return resolveMsg(res);
    }



    /**
     * 查询消息列表
     * {
     *     "object": "list",
     *     "data": [
     *         {
     *             "id": "msg_bKCLpNSUV63QkleTFBrDLt0M",
     *             "object": "thread.message",
     *             "created_at": 1703688486,
     *             "thread_id": "thread_zR7atctaC3yfbcQg3zvFPikV",
     *             "role": "user",
     *             "content": [
     *                 {
     *                     "type": "text",
     *                     "text": {
     *                         "value": "纳达尔是谁",
     *                         "annotations": []
     *                     }
     *                 }
     *             ],
     *             "file_ids": [],
     *             "assistant_id": null,
     *             "run_id": null,
     *             "metadata": {}
     *         }
     *     ],
     *     "first_id": "msg_bKCLpNSUV63QkleTFBrDLt0M",
     *     "last_id": "msg_bKCLpNSUV63QkleTFBrDLt0M",
     *     "has_more": false
     * }
     */
    public static ChatThreadMsgModel queryAllMessage(OpenAiConfigDto configDto, String treadId){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/messages";
        JSONObject body = new JSONObject();

        String value = HttpProxyUtils.get(
                url,
                configDto.initHeader(),
                body,
                OpenAiConstant.DEFAULT_TIMEOUT);

        log.info("请求url={},value={}",url,value);


        JSONObject js = JSON.parseObject(value);


        ChatThreadMsgModel model = new ChatThreadMsgModel();
        model.setFirstId(js.getString("first_id"));
        model.setLastId(js.getString("last_id"));
        model.setHasMore(js.getBoolean("has_more"));

        JSONArray dataList = js.getJSONArray("data");
        for (int i=0;i<dataList.size();i++){
            try {
                JSONObject obj = dataList.getJSONObject(i);
                ChatMessageDto dto = resolveMsg(obj);
                model.getData().add(dto);
            }catch (Exception e){
                log.error("数据解析异常");
            }
        }
        return model;
    }


    /**
     *
     * @param configDto
     * @param treadId
     * @param msgId
     * @return
     * {
     *     "id": "msg_L0WJ2QBIdUsUqyl5wxNQ9Wmx",
     *     "object": "thread.message",
     *     "created_at": 1704377387,
     *     "thread_id": "thread_D7sqy2dt6eZUC0MkmqUu50zD",
     *     "role": "assistant",
     *     "content": [
     *         {
     *             "type": "text",
     *             "text": {
     *                 "value": "很抱歉，看来我之前提到的“GOTA”在网球领域中并没有固定的定义。可能这个词语在网球术语中并不常见，或者它可能代表的是某个特定的人、地点或事件，而这些信息目前并不在我的范围内。\n\n如果你可以提供更多上下文、特定术语或其他信息，我将竭诚为你提供更准确的信息。希望我能为你提供更多帮助！",
     *                 "annotations": []
     *             }
     *         }
     *     ],
     *     "file_ids": [],
     *     "assistant_id": "asst_WhFAtHUc3gahhsHBPHRnXBxR",
     *     "run_id": "run_MybJhM8ny4pZOvwiUZSjwMMo",
     *     "metadata": {}
     * }
     */
    public static ChatMessageDto queryOneMessage(OpenAiConfigDto configDto, String treadId,String msgId){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/messages/"+msgId;
        JSONObject body = new JSONObject();

        String value = HttpProxyUtils.get(
                url,
                configDto.initHeader(),
                body,
                OpenAiConstant.DEFAULT_TIMEOUT);
        log.info("请求url={},value={}",url,value);

        JSONObject js = JSON.parseObject(value);
        return resolveMsg(js);
    }


    /**
     * 创建信息
     * @param obj
     * @return
     */
    private static ChatMessageDto resolveMsg(JSONObject obj){

        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(obj.getString("id"));
        dto.setRole(obj.getString("role"));

        JSONArray content = obj.getJSONArray("content");
        if (content != null && content.size() > 0){
            JSONObject cObj = content.getJSONObject(0);
            dto.setMessageType(cObj.getString("type"));
            dto.setContent(cObj.getJSONObject("text").getString("value"));
        }
        return dto;
    }

}
