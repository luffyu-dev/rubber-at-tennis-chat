package com.rubber.at.tennis.chat.manager.chat;

import com.alibaba.fastjson2.JSON;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.openai.AssistantsMessageManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsRunManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsThreadManager;
import com.rubber.base.components.util.result.ResultMsg;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
public class ChatGptTest {




    public static void main(String[] args) {

        OpenAiConfigDto dto = new OpenAiConfigDto();
        String value1 = AssistantsThreadManager.createChatThread(dto);

        System.out.println(value1);
        String treadId =  JSON.parseObject(value1).getString("id");

        Object value2 = AssistantsMessageManager.addMessage(dto,treadId,"纳达尔是谁");
        System.out.println(value2);

        String value3 = AssistantsRunManager.runTread(dto,treadId);
        String runId = JSON.parseObject(value3).getString("id");
        System.out.println(value3);

        Object value4 = AssistantsRunManager.queryRunStatus(dto,treadId,runId);
        System.out.println(value4);


        Object value5 = AssistantsMessageManager.queryAllMessage(dto,treadId);
        System.out.println(value5);

    }


}
