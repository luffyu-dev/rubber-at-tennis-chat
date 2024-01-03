package com.rubber.at.tennis.chat.api.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2023/12/22
 */
@Data
public class SendChatGptResult {


    /**
     * 线程id
     */
    private String threadId;

    /**
     * 结果id
     */
    private String runId;

}
