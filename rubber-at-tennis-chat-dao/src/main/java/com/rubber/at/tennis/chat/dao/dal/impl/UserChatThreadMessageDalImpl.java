package com.rubber.at.tennis.chat.dao.dal.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rubber.at.tennis.chat.dao.entity.UserChatThreadMessageEntity;
import com.rubber.at.tennis.chat.dao.mapper.UserChatThreadMessageMapper;
import com.rubber.at.tennis.chat.dao.dal.IUserChatThreadMessageDal;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 聊天内容 服务实现类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
@Service
public class UserChatThreadMessageDalImpl extends BaseAdminService<UserChatThreadMessageMapper, UserChatThreadMessageEntity> implements IUserChatThreadMessageDal {

    /**
     * @param uid
     * @param threadId
     * @param msgId
     * @return
     */
    @Override
    public UserChatThreadMessageEntity queryMsgStatus(Integer uid, String threadId, String msgId) {

        LambdaQueryWrapper<UserChatThreadMessageEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserChatThreadMessageEntity::getUid,uid)
                .eq(UserChatThreadMessageEntity::getThreadId,threadId)
                .eq(UserChatThreadMessageEntity::getMsgId,msgId);
        return getOne(lqw);
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public UserChatThreadMessageEntity saveOrUpdateByEntity(UserChatThreadMessageEntity entity) {
        UserChatThreadMessageEntity oldMsg = queryMsgStatus(entity.getUid(), entity.getThreadId(), entity.getMsgId());
        if (oldMsg == null){
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            save(entity);
            return entity;
        }
        BeanUtils.copyProperties(entity,oldMsg,"id","createTime","createTime");
        entity.setUpdateTime(new Date());
        updateById(oldMsg);
        return oldMsg;
    }
}
