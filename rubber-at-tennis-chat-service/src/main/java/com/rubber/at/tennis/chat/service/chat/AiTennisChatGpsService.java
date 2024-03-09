package com.rubber.at.tennis.chat.service.chat;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rubber.at.tennis.chat.api.AiTennisChatGptApi;
import com.rubber.at.tennis.chat.api.constant.ChatConstant;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.UserChatLimitDto;
import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import com.rubber.at.tennis.chat.api.dto.message.ChatThreadMsgModel;
import com.rubber.at.tennis.chat.api.dto.message.MsgChatReq;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsDto;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsStatusDto;
import com.rubber.at.tennis.chat.api.dto.thread.ChatThreadDto;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.at.tennis.chat.api.enums.MsgRoleEnums;
import com.rubber.at.tennis.chat.api.enums.MsgStatusEnums;
import com.rubber.at.tennis.chat.dao.dal.IUserChatLimitDal;
import com.rubber.at.tennis.chat.dao.dal.IUserChatThreadDal;
import com.rubber.at.tennis.chat.dao.dal.IUserChatThreadMessageDal;
import com.rubber.at.tennis.chat.dao.entity.UserChatLimitEntity;
import com.rubber.at.tennis.chat.dao.entity.UserChatThreadEntity;
import com.rubber.at.tennis.chat.dao.entity.UserChatThreadMessageEntity;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import com.rubber.at.tennis.chat.manager.openai.AssistantsMessageManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsRunManager;
import com.rubber.at.tennis.chat.manager.openai.AssistantsThreadManager;
import com.rubber.at.tennis.chat.service.common.exception.ErrorCodeEnums;
import com.rubber.at.tennis.chat.service.common.exception.RubberServiceException;
import com.rubber.at.tennis.chat.service.component.AiTennisConfigComponent;
import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.session.BaseUserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private IUserChatThreadMessageDal iUserChatThreadMessageDal;

    @Autowired
    private IUserChatLimitDal iUserChatLimitDal;

    /**
     * @param userSession
     * @return
     */
    @Override
    public UserChatLimitDto queryUserLimit(BaseUserSession userSession) {
        UserChatLimitDto dto = new UserChatLimitDto();
        UserChatLimitEntity userChatLimit = iUserChatLimitDal.getByUid(userSession.getUid());
        if (userChatLimit == null){
            return dto;
        }
        BeanUtils.copyProperties(userChatLimit,dto);
        String day = DateUtil.format(new DateTime(),"yyyyMMdd");
        if (!day.equals(userChatLimit.getLimitDay())){
            dto.setUsageMsgNum(0);
            dto.setLimitMsgNum(ChatConstant.DEFAULT_CHAT_DAY_LIMIT);
        }
        return dto;
    }

    /**
     * @param userSession
     */
    @Override
    public void addUserLimit(BaseUserSession userSession) {
        UserChatLimitEntity userChatLimit = iUserChatLimitDal.getByUid(userSession.getUid());
        if (userChatLimit == null){
            return;
        }
        userChatLimit.setLimitMsgNum(userChatLimit.getLimitMsgNum() + ChatConstant.DEFAULT_ADD_CHAT_LIMIT);
        userChatLimit.setUpdateTime(new Date());
        if(!iUserChatLimitDal.updateById(userChatLimit)){
            throw new RubberServiceException(SysCode.SYSTEM_BUS);
        }
    }

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
     * 删除线程
     *
     * @param req
     */
    @Override
    public void removeChatThreads(ThreadChatReq req) {
        if (StrUtil.isEmpty(req.getThreadId())){
            return ;
        }
        UserChatThreadEntity chatThreadEntity = iUserChatThreadDal.getByThreadId(req.getUid(), req.getThreadId());
        if (chatThreadEntity != null){
            chatThreadEntity.setStatus(0);
            iUserChatThreadDal.updateById(chatThreadEntity);
        }
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

        //OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();

        Page<UserChatThreadMessageEntity> page = new Page<>();
        page.setCurrent(req.getPage());
        page.setSize(req.getSize());
        page.setSearchCount(false);

        LambdaQueryWrapper<UserChatThreadMessageEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserChatThreadMessageEntity::getThreadId,req.getThreadId())
                .eq(UserChatThreadMessageEntity::getUid,req.getUid())
                .orderByDesc(UserChatThreadMessageEntity::getId);
        iUserChatThreadMessageDal.page(page, lqw);

        if (CollectionUtils.isEmpty(page.getRecords())){
            return new ChatThreadMsgModel();
        }

        ChatThreadMsgModel model = new ChatThreadMsgModel();
        model.setFirstId(page.getRecords().get(0).getMsgId());
        model.setData(
            page.getRecords().stream().map(this::convertByEntity).collect(Collectors.toList())
        );
        return model;
        //return AssistantsMessageManager.queryAllMessage(configDto, req.getThreadId());
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
        // 从db中查到了数据
        UserChatThreadMessageEntity msgEntity = iUserChatThreadMessageDal.queryMsgStatus(req.getUid(), req.getThreadId(), req.getMsgId());
        if (msgEntity != null && !MsgStatusEnums.COMPLETED.getStatus().equals(msgEntity.getStatus())){
            return convertByEntity(msgEntity);
        }
        // 从OPENAI中查询数据
        OpenAiConfigDto configDto = aiTennisConfigComponent.initAndCreateConfig();
        ChatMessageDto dto =  AssistantsMessageManager.queryOneMessage(configDto,req.getThreadId(),req.getMsgId());
        // 如果成功，则写入到db中
        if (StrUtil.isNotEmpty(dto.getContent())){
            UserChatThreadMessageEntity message = convertByDto(dto,req);
            message.setRole(MsgRoleEnums.ASSISTANTS.getRole());
            message.setStatus(MsgStatusEnums.COMPLETED.getStatus());
            iUserChatThreadMessageDal.saveOrUpdateByEntity(message);
        }
        return dto;
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
        // 判断今天是否还有次数
        if(!iUserChatLimitDal.updateBySendMsg(req.getUid())){
            // 抛出异常 没有次数
            throw new RubberServiceException(ErrorCodeEnums.SEND_MSG_LIMITED);
        }

        // 发送消息
        ChatMessageDto dto = AssistantsMessageManager.addMessage(configDto, req.getThreadId(), req.getMessage());
        // 执行运行
        String runId = AssistantsRunManager.runTread(configDto, req.getThreadId());

        // 记录用户发到到消息到db中
        UserChatThreadMessageEntity userChatThreadMessage = convertByDto(dto,req);
        userChatThreadMessage.setRole(MsgRoleEnums.USER.getRole());
        userChatThreadMessage.setContent(req.getMessage());
        userChatThreadMessage.setStatus(MsgStatusEnums.COMPLETED.getStatus());
        iUserChatThreadMessageDal.saveOrUpdateByEntity(userChatThreadMessage);

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
        if (StringUtils.isEmpty(treadId)){
            throw new RubberServiceException(SysCode.PARAM_ERROR);
        }
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
        // 创建线程的时候记录线程id
        iUserChatLimitDal.updateByCreateThread(req.getUid());
        return treadId;
    }


    private void doUpdateUserChat(SendMessageReq req){
        UserChatThreadEntity chatThreadEntity = iUserChatThreadDal.getByThreadId(req.getUid(), req.getThreadId());
        if (chatThreadEntity == null){
            throw new RubberServiceException(SysCode.PARAM_ERROR);
        }
        chatThreadEntity.setUpdateTime(new Date());

        if(!iUserChatThreadDal.updateById(chatThreadEntity)){
            log.error("更新失败 req={}",req);
        }

    }


    private ChatMessageDto convertByEntity(UserChatThreadMessageEntity entity){
        if (entity == null){
            return new ChatMessageDto();
        }
        ChatMessageDto dto = new ChatMessageDto();
        BeanUtils.copyProperties(entity,dto,"id");
        dto.setId(entity.getMsgId());
        return dto;
    }



    private UserChatThreadMessageEntity convertByDto(ChatMessageDto dto, ThreadChatReq req){
        UserChatThreadMessageEntity entity = new UserChatThreadMessageEntity();
        BeanUtils.copyProperties(dto,entity,"id");
        entity.setMsgId(dto.getId());
        entity.setUid(req.getUid());
        entity.setThreadId(req.getThreadId());
        if (StrUtil.isNotEmpty(entity.getContent())){
            entity.setStatus(MsgStatusEnums.COMPLETED.getStatus());
        }
        return entity;
    }
}
