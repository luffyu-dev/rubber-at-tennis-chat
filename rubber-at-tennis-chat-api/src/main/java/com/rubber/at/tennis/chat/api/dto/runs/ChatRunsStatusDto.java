package com.rubber.at.tennis.chat.api.dto.runs;

import com.rubber.at.tennis.chat.api.dto.message.ChatMessageDto;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@Data
public class ChatRunsStatusDto {

    /**
     * 线程id
     */
    private String threadId;


    /**
     * 执行id
     */
    private String runId;


    /**
     * run执行的状态
     */
    private String status;


    /**
     * 执行成功之后的消息id
     */
    private String msgId;
}
