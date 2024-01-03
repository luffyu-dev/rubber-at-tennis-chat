package com.rubber.at.tennis.chat.service.chat;

import cn.hutool.core.util.StrUtil;
import com.rubber.at.tennis.chat.api.AiTennisChatGptApi;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import com.rubber.at.tennis.chat.api.dto.message.ChatThreadMsgModel;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsDto;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.thread.ChatThreadDto;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.openai.AssistantsMessageManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsRunManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsThreadManager;
import com.rubber.at.tennis.chat.service.component.AiTennisConfigComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@Slf4j
@Service
public class AiTennisChatGpsService implements AiTennisChatGptApi {

    @Autowired
    private AiTennisConfigComponent aiTennisConfigComponent;

    /**
     * 查询线程list
     *
     * @param chatReq
     * @return
     */
    @Override
    public List<ChatThreadDto> queryAssistantsThreads(BaseChatReq chatReq) {
        return null;
    }

    /**
     * 查询一个线程的消息列表
     *
     * @param req
     * @return
     */
    @Override
    public ChatThreadMsgModel queryChatMessageList(ThreadChatReq req) {
        if (StrUtil.isEmpty(req.getThreadId())){
            return new ChatThreadMsgModel();
        }

        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();
        return AssistantsMessageManager.queryAllMessage(configDto, req.getThreadId());
    }

    /**
     * 发送消息列表
     *
     * @param req
     * @return
     */
    @Override
    public ChatRunsDto sendMessage(SendMessageReq req) {
        if (StrUtil.isEmpty(req.getThreadId())){
            return new ChatRunsDto();
        }
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();
        if (req.isNewChat()){
            String treadId = AssistantsThreadManager.createChatThread(configDto);
            req.setThreadId(treadId);
        }
        // 发送消息
        ChatMessageDto dto = AssistantsMessageManager.addMessage(configDto, req.getThreadId(), req.getMessage());

        // 执行运行
        String runId = AssistantsRunManager.runTread(configDto, req.getThreadId());
        ChatRunsDto chatRunsDto = new ChatRunsDto();
        chatRunsDto.setRunId(runId);
        chatRunsDto.setThreadId(req.getThreadId());
        chatRunsDto.setMsgData(dto);
        return chatRunsDto;
    }

    /**
     * 查询状态
     *
     * @param req
     * @return
     */
    @Override
    public ChatRunsDto queryRunsStatus(ChatRunsReq req) {
        if (StrUtil.isEmpty(req.getThreadId()) || StrUtil.isEmpty(req.getRunId())){
            return new ChatRunsDto();
        }
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();

        return AssistantsRunManager.queryRunStatus(configDto,req.getThreadId(),req.getRunId());

    }
}
