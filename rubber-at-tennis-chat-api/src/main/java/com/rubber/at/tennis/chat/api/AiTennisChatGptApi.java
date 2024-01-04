package com.rubber.at.tennis.chat.api;

import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.message.ChatThreadMsgModel;
import com.rubber.at.tennis.chat.api.dto.message.MsgChatReq;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsStatusDto;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsDto;
import com.rubber.at.tennis.chat.api.dto.thread.ChatThreadDto;

import java.util.List;

/**
 * @author luffyu
 * Created on 2022/8/14
 */
public interface AiTennisChatGptApi {



    /**
     * 查询线程list
     * @param chatReq
     * @return
     */
    List<ChatThreadDto> queryAssistantsThreads(BaseChatReq chatReq);



    /**
     * 查询消息列表
     * @param req
     * @return
     */
    ChatThreadMsgModel queryChatMessageList(ThreadChatReq req);


    /**
     * 查询单条消息
     * @param req
     * @return
     */
    ChatMessageDto querySingleMsg(MsgChatReq req);

    /**
     * 发送消息列表
     * @param req
     * @return
     */
    ChatRunsDto sendMessage(SendMessageReq req);


    /**
     * 查询状态
     * @param req
     * @return
     */
    ChatRunsStatusDto queryRunsStatus(ChatRunsReq req);


}
