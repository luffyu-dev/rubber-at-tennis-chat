package com.rubber.at.tennis.chat.api.dto.message;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2023/12/28
 */
@Data
public class ChatMessageDto {


    /**
     * 消息id
     */
    private String id;

    /**
     * user表示用户
     * assistant 表示助手
     */
    private String role;

    /**
     * 消息内容
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String content;


    /**
     * 线程id
     */
    private String treadId;

//    /**
//     * 返回的消息体
//     */
//    private List<JSONObject> content;
}
