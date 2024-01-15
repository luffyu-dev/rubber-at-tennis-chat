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
 * 字段数据表
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_global_field_dict")
public class GlobalFieldDictEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 字段的key值
     */
    @TableField("Ffield_key")
    private String fieldKey;

    /**
     * 字段的value值
     */
    @TableField("Ffield_value")
    private String fieldValue;

    /**
     * 字段的名称值
     */
    @TableField("Ffield_name")
    private String fieldName;

    /**
     * 字段状态 10表示正常
     */
    @TableField("Fstate")
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date modifyTime;

    /**
     * 版本号
     */
    @Version
    private Integer version;


}
