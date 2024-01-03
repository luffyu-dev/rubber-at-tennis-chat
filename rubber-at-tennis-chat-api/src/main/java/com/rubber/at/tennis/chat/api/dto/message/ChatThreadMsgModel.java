package com.rubber.at.tennis.chat.api.dto.message;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个线程的消息列表
 * @author luffyu
 * Created on 2024/1/1
 */
@Data
public class ChatThreadMsgModel {
    /**
     * 第一条的消息Id
     */
    private String firstId;

    /**
     * 最后一个消息id
     */
    private String lastId;

    /**
     * 是否还有更多
     */
    private boolean hasMore;


    /**
     * 消息体
     */
    private List<ChatMessageDto> data = new ArrayList<>();
}
