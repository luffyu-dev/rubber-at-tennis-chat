package com.rubber.at.tennis.chat.manager.openai;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.utils.HttpProxyUtils;
import com.rubber.at.tennis.chat.manager.utils.OpenAiConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * openAI的Assistants助手的线程管理
 * @author luffyu
 * Created on 2023/12/27
 */
@Slf4j
public class AssistantsThreadManager {

    /**
     * 创建一个聊天线程id
     * @return
     */
    public static String createChatThread(OpenAiConfigDto configDto){
        String url = OpenAiConstant.BASE_URL  + "/v1/threads";
        JSONObject body = new JSONObject();
        String value = HttpProxyUtils.post(
                url,
                configDto.initHeader(),
                body,
                OpenAiConstant.DEFAULT_TIMEOUT);
        log.info("请求url={},value={}",url,value);
        JSONObject re = JSONObject.parseObject(value);
        return re.getString("id");
    }



}
