package com.rubber.at.tennis.chat.api.dto.message;

import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageReq extends ThreadChatReq {


    /**
     * 是否是新的聊天框
     */
    private boolean isNewChat = false;

    /**
     * 消息的内容
     */
    private String message;
}
