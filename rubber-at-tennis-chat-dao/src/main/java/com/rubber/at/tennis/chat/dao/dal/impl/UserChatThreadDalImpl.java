package com.rubber.at.tennis.chat.dao.dal.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rubber.at.tennis.chat.dao.entity.UserChatThreadEntity;
import com.rubber.at.tennis.chat.dao.mapper.UserChatThreadMapper;
import com.rubber.at.tennis.chat.dao.dal.IUserChatThreadDal;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 聊天线程记录信息 服务实现类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
@Service
public class UserChatThreadDalImpl extends BaseAdminService<UserChatThreadMapper, UserChatThreadEntity> implements IUserChatThreadDal {

    /**
     * @param uid
     * @return
     */
    @Override
    public List<UserChatThreadEntity> queryUserChatThread(Integer uid) {
        if (uid == null || uid <= 0){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<UserChatThreadEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserChatThreadEntity::getUid,uid)
                .eq(UserChatThreadEntity::getStatus,10)
                .orderByDesc(UserChatThreadEntity::getUpdateTime);
        return list(lqw);
    }

    /**
     * @param uid
     * @param threadId
     * @return
     */
    @Override
    public UserChatThreadEntity getByThreadId(Integer uid, String threadId) {
        LambdaQueryWrapper<UserChatThreadEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserChatThreadEntity::getUid,uid)
                .eq(UserChatThreadEntity::getStatus,10)
                .eq(UserChatThreadEntity::getThreadId,threadId);
        return getOne(lqw);
    }
}
