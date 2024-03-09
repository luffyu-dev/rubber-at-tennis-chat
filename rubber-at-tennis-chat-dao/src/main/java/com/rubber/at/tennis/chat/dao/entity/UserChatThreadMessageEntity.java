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
 * 聊天内容
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_chat_thread_message")
public class UserChatThreadMessageEntity extends BaseEntity {

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
     * 线程id
     */
    @TableField("Fthread_id")
    private String threadId;

    /**
     * 消息id
     */
    @TableField("Fmsg_id")
    private String msgId;

    /**
     * 聊天角色 user / assistants
     */
    @TableField("Frole")
    private String role;

    /**
     * 内容
     */
    @TableField("Fcontent")
    private String content;

    /**
     * 拓展参数
     */
    @TableField("Fext_params")
    private String extParams;

    /**
     * 状态 in_progress  completed 完成
     */
    @TableField("Fstatus")
    private String status;

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
