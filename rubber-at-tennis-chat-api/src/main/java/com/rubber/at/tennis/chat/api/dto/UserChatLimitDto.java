package com.rubber.at.tennis.chat.api.dto;

import com.rubber.at.tennis.chat.api.constant.ChatConstant;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2024/1/21
 */
@Data
public class UserChatLimitDto {


    /**
     * 用户uid
     */
    private Integer uid;

    /**
     * 限制天数
     */
    private String limitDay;

    /**
     * 限制的总消息数量
     */
    private Integer limitMsgNum = ChatConstant.DEFAULT_CHAT_DAY_LIMIT;

    /**
     * 总消息数
     */
    private Integer usageMsgNum = 0;

    /**
     * 总线程数
     */
    private Integer allUsageThreadNum;

    /**
     * 总消息数
     */
    private Integer allUsageMsgNum;

    /**
     * 总token数
     */
    private Integer allUsageTokenNum;
}
