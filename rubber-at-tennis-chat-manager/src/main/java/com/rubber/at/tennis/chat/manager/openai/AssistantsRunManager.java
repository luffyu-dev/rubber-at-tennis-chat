package com.rubber.at.tennis.chat.manager.openai;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsStatusDto;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.utils.HttpProxyUtils;
import com.rubber.at.tennis.chat.manager.utils.OpenAiConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
@Slf4j
public class AssistantsRunManager {


    /**
     * 执行线程
     * @param treadId
     * @return
     *
     * 返回runId
     */
    public static String runTread(OpenAiConfigDto configDto, String treadId){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/runs";
        JSONObject body = new JSONObject();
        body.put("assistant_id",configDto.getAssistantId());
        String value = HttpProxyUtils.post(
                url,
                configDto.initHeader(),
                body,
                OpenAiConstant.DEFAULT_TIMEOUT);
        log.info("请求url={},value={}",url,value);
        return JSONObject.parseObject(value).getString("id");
    }


    /**
     * 查询线程状态
     * @param configDto
     * @param treadId
     * @param runId
     * @return
     *
     * {
     *     "object": "list",
     *     "data": [
     *         {
     *             "id": "step_qRWlMgzyzLiOgPA4ntkKMI0K",
     *             "object": "thread.run.step",
     *             "created_at": 1704377816,
     *             "run_id": "run_cnlfbXZed44cDS6m1XYqfRH4",
     *             "assistant_id": "asst_WhFAtHUc3gahhsHBPHRnXBxR",
     *             "thread_id": "thread_D7sqy2dt6eZUC0MkmqUu50zD",
     *             "type": "message_creation",
     *             "status": "completed",
     *             "cancelled_at": null,
     *             "completed_at": 1704377825,
     *             "expires_at": null,
     *             "failed_at": null,
     *             "last_error": null,
     *             "step_details": {
     *                 "type": "message_creation",
     *                 "message_creation": {
     *                     "message_id": "msg_sDJotoc2GnhfnSzoVie56l9R"
     *                 }
     *             }
     *         }
     *     ],
     *     "first_id": "step_qRWlMgzyzLiOgPA4ntkKMI0K",
     *     "last_id": "step_qRWlMgzyzLiOgPA4ntkKMI0K",
     *     "has_more": false
     * }
     */
    public static ChatRunsStatusDto queryRunStatus(OpenAiConfigDto configDto, String treadId, String runId){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/runs/"+runId+"/steps";
        String value = HttpProxyUtils.get(
                url,
                configDto.initHeader(),
                new JSONObject(),
                OpenAiConstant.DEFAULT_TIMEOUT);

        log.info("请求url={},value={}",url,value);

        ChatRunsStatusDto dto = new ChatRunsStatusDto();
        dto.setThreadId(treadId);
        dto.setRunId(runId);

        JSONObject js = JSONObject.parseObject(value);
        JSONArray dataList = js.getJSONArray("data");
        if (dataList != null && dataList.size() > 0){
            JSONObject data = dataList.getJSONObject(0);
            dto.setStatus(data.getString("status"));
            if ("completed".equals(dto.getStatus())){
                JSONObject stepDetails = data.getJSONObject("step_details");
                String type = stepDetails.getString("type");
                String messageId = stepDetails.getJSONObject(type).getString("message_id");
                dto.setMsgId(messageId);
            }
        }
        return dto;
    }
}
