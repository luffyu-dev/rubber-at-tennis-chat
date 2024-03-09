package com.rubber.at.tennis.chat.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rubber.base.components.mysql.plugins.admin.bean.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * chat聊天限制数
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_chat_limit")
public class UserChatLimitEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "Fid", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户uid
     */
    @TableField("Fuid")
    private Integer uid;

    /**
     * 限制天数
     */
    @TableField("Flimit_day")
    private String limitDay;

    /**
     * 限制的总消息数量
     */
    @TableField("Flimit_msg_num")
    private Integer limitMsgNum;

    /**
     * 总消息数
     */
    @TableField("Fusage_msg_num")
    private Integer usageMsgNum;

    /**
     * 总线程数
     */
    @TableField("Fall_usage_thread_num")
    private Integer allUsageThreadNum;

    /**
     * 总消息数
     */
    @TableField("Fall_usage_msg_num")
    private Integer allUsageMsgNum;

    /**
     * 总token数
     */
    @TableField("Fall_usage_token_num")
    private Integer allUsageTokenNum;

    /**
     * 版本号
     */
    @TableField("Fversion")
    private Integer version;

    /**
     * 状态 10表示报名成功 20表示退出
     */
    @TableField("Fstatus")
    private Integer status;

    /**
     * 报名时间
     */
    @TableField("Fcreate_time")
    private Date createTime;

    /**
     * 报名时间
     */
    @TableField("Fupdate_time")
    private Date updateTime;


}
