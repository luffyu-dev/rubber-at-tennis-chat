package com.rubber.at.tennis.chat.service.chat;

import cn.hutool.core.util.StrUtil;
import com.rubber.at.tennis.chat.api.AiTennisChatGptApi;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import com.rubber.at.tennis.chat.api.dto.message.ChatThreadMsgModel;
import com.rubber.at.tennis.chat.api.dto.message.MsgChatReq;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsDto;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsStatusDto;
import com.rubber.at.tennis.chat.api.dto.thread.ChatThreadDto;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.at.tennis.chat.dao.dal.IUserChatThreadDal;
import com.rubber.at.tennis.chat.dao.entity.UserChatThreadEntity;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.openai.AssistantsMessageManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsRunManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsThreadManager;
import com.rubber.at.tennis.chat.service.common.exception.RubberServiceException;
import com.rubber.at.tennis.chat.service.component.AiTennisConfigComponent;
import com.rubber.base.components.util.result.code.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@Slf4j
@Service
public class AiTennisChatGpsService implements AiTennisChatGptApi {

    @Autowired
    private AiTennisConfigComponent aiTennisConfigComponent;

    @Autowired
    private IUserChatThreadDal iUserChatThreadDal;

    /**
     * 查询线程list
     *
     * @param chatReq
     * @return
     */
    @Override
    public List<ChatThreadDto> queryAssistantsThreads(BaseChatReq chatReq) {
        if (chatReq.getUid() == null || chatReq.getUid() <= 0){
            return new ArrayList<>();
        }
        List<UserChatThreadEntity> userChatThreadEntities = iUserChatThreadDal.queryUserChatThread(chatReq.getUid());
        if (CollectionUtils.isEmpty(userChatThreadEntities)){
            return new ArrayList<>();
        }
        return userChatThreadEntities.stream().map(i->{
            ChatThreadDto dto = new ChatThreadDto();
            BeanUtils.copyProperties(i,dto);
            return dto;
        }).collect(Collectors.toList());
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
     * 查询单条消息
     *
     * @param req
     * @return
     */
    @Override
    public ChatMessageDto querySingleMsg(MsgChatReq req) {
        if (StrUtil.isEmpty(req.getThreadId()) || StrUtil.isEmpty(req.getMsgId())){
            return new ChatMessageDto();
        }
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();
        return AssistantsMessageManager.queryOneMessage(configDto,req.getThreadId(),req.getMsgId());
    }

    /**
     * 发送消息列表
     *
     * @param req
     * @return
     */
    @Override
    public ChatRunsDto sendMessage(SendMessageReq req) {
        if (StrUtil.isEmpty(req.getThreadId()) && !req.isNewChat()){
            return new ChatRunsDto();
        }
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();
        if (req.isNewChat()){
            String treadId = doCreateNewChat(configDto,req);
            req.setThreadId(treadId);
        }else {
            // 更新聊天
            doUpdateUserChat(req);
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
    public ChatRunsStatusDto queryRunsStatus(ChatRunsReq req) {
        if (StrUtil.isEmpty(req.getThreadId()) || StrUtil.isEmpty(req.getRunId())){
            return new ChatRunsStatusDto();
        }
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();

        return AssistantsRunManager.queryRunStatus(configDto,req.getThreadId(),req.getRunId());

    }


    /**
     * 创建新线程
     * @param configDto
     * @param req
     * @return
     */
    private String doCreateNewChat(OpenAiConfigDto configDto,SendMessageReq req){
        String treadId = AssistantsThreadManager.createChatThread(configDto);
        UserChatThreadEntity userChatThreadEntity = new UserChatThreadEntity();
        userChatThreadEntity.setUid(req.getUid());
        userChatThreadEntity.setThreadId(treadId);
        String  name = req.getMessage();
        if (name.length() > 64){
            name = name.substring(0,64);
        }
        userChatThreadEntity.setChatName(name);
        userChatThreadEntity.setCreateTime(new Date());
        userChatThreadEntity.setUpdateTime(new Date());
        if(!iUserChatThreadDal.save(userChatThreadEntity)){
            throw new RubberServiceException(SysCode.SYSTEM_BUS);
        }
        return treadId;
    }


    private void doUpdateUserChat(SendMessageReq req){
        UserChatThreadEntity chatThreadEntity = iUserChatThreadDal.getByThreadId(req.getUid(), req.getMessage());
        if (chatThreadEntity == null){
            throw new RubberServiceException(SysCode.PARAM_ERROR);
        }
        chatThreadEntity.setUpdateTime(new Date());

        if(!iUserChatThreadDal.updateById(chatThreadEntity)){
            log.error("更新失败 req={}",req);
        }

    }
}
