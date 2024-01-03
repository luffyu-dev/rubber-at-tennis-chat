package com.rubber.at.tennis.chat.manager.openai;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsDto;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.utils.HttpProxyUtils;
import com.rubber.at.tennis.chat.manager.utils.OpenAiConstant;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
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
     *     "id": "run_cUzIoW6TmnmSmylXvxZ0Egqr",
     *     "object": "thread.run",
     *     "created_at": 1703688630,
     *     "assistant_id": "asst_WhFAtHUc3gahhsHBPHRnXBxR",
     *     "thread_id": "thread_zR7atctaC3yfbcQg3zvFPikV",
     *     "status": "expired",
     *     "metadata": {}
     * }
     */
    public static ChatRunsDto queryRunStatus(OpenAiConfigDto configDto, String treadId, String runId){
        String url = OpenAiConstant.BASE_URL + "/v1/threads/"+treadId+"/runs/"+runId;
        String value = HttpProxyUtils.get(
                url,
                configDto.initHeader(),
                new JSONObject(),
                OpenAiConstant.DEFAULT_TIMEOUT);

        JSONObject js = JSONObject.parseObject(value);

        ChatRunsDto dto = new ChatRunsDto();
        dto.setThreadId(treadId);
        dto.setRunId(runId);
        dto.setStatus(js.getString("status"));
        return dto;
    }
}
