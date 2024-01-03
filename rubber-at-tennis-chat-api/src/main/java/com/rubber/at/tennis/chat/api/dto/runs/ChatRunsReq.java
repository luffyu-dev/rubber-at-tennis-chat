package com.rubber.at.tennis.chat.api.dto.runs;

import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatRunsReq extends ThreadChatReq {

    /**
     * 执行id
     */
    private String runId;
}
