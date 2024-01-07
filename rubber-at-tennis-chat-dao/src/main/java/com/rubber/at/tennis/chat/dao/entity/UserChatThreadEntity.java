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
 * 聊天线程记录信息
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_chat_thread")
public class UserChatThreadEntity extends BaseEntity {

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
     * 线程名称
     */
    @TableField("Fchat_name")
    private String chatName;

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
