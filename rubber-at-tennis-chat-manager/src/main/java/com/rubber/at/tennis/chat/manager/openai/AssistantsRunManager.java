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
     *
     * queued：Run首次创建或完成所需操作后，将其移至排队状态。它们几乎立即转到in_progress状态。
     * in_progress：助手使用模型和工具执行步骤。您可以通过检查Run Steps来查看Run的进度。
     * completed：运行成功完成！您现在可以查看Assistant添加到Thread中的所有Message以及运行所采取的所有步骤。您还可以通过向Thread添加更多用户消息并创建另一个Run来继续对话。
     * requires_action：当使用function-calling工具时，一旦模型确定要调用的函数的名称和参数，运行将转移到requires_action状态。
     * expired：当函数调用输出在expires_at之前未提交并且运行过期时，会发生这种情况。此外，如果运行时间太长，超过expires_at中所述的时间，我们的系统将使运行过期。
     * cancelling：您可以尝试使用https://platform.openai.com/docs/api-reference/runs/cancelRun端点取消正在进行中的运行
     * cancelled：运行已成功取消。
     * failed：您可以通过查看Run中的last_error对象来查看失败的原因。失败的时间戳将记录在failed_at下。
     *
     *
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
