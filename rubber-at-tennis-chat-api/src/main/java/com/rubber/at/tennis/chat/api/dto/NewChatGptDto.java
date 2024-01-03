package com.rubber.at.tennis.chat.api.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2023/12/22
 */
@Data
public class NewChatGptDto {

    /**
     * 线程id
     */
    private String threadId;

    /**
     * 发送的消息
     */
    private String message;
}
