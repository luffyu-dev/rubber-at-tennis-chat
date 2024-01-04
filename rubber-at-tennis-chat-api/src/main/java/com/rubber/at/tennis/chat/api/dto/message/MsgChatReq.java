package com.rubber.at.tennis.chat.api.dto.message;

import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luffyu
 * Created on 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MsgChatReq extends ThreadChatReq {

    private String msgId;
}
