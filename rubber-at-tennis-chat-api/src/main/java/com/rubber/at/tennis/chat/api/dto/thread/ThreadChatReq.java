package com.rubber.at.tennis.chat.api.dto.thread;

import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ThreadChatReq  extends BaseChatReq {

    /**
     * 线程id
     */
    private String threadId;
}
