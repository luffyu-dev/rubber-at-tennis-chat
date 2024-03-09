package com.rubber.at.tennis.chat.dao.dal.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rubber.at.tennis.chat.api.constant.ChatConstant;
import com.rubber.at.tennis.chat.dao.entity.UserChatLimitEntity;
import com.rubber.at.tennis.chat.dao.mapper.UserChatLimitMapper;
import com.rubber.at.tennis.chat.dao.dal.IUserChatLimitDal;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * chat聊天限制数 服务实现类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
@Service
public class UserChatLimitDalImpl extends BaseAdminService<UserChatLimitMapper, UserChatLimitEntity> implements IUserChatLimitDal {

    /**
     * @param uid
     * @return
     */
    @Override
    public UserChatLimitEntity getByUid(Integer uid) {
        if(uid == null || uid <= 0){
            return null;
        }
        LambdaQueryWrapper<UserChatLimitEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserChatLimitEntity::getUid,uid);
        return getOne(lqw);
    }

    /**
     * @param uid
     * @param msgNum
     * @return
     */
    @Override
    public boolean updateBySendMsg(Integer uid) {
        String day = DateUtil.format(new DateTime(),"yyyyMMdd");
        boolean saveFlag = false;
        UserChatLimitEntity userChatLimit = getByUid(uid);
        if (userChatLimit == null){
            userChatLimit = initLimit(uid,day);
            saveFlag = true;
        }
        // 是同一天，并且次数一样
        if (day.equals(userChatLimit.getLimitDay()) && userChatLimit.getLimitMsgNum() <= userChatLimit.getUsageMsgNum()){
            return false;
        }
        if (!day.equals(userChatLimit.getLimitDay())){
            userChatLimit.setLimitDay(day);
            userChatLimit.setLimitMsgNum(ChatConstant.DEFAULT_CHAT_DAY_LIMIT);
            userChatLimit.setUsageMsgNum(0);
        }
        userChatLimit.setUsageMsgNum(userChatLimit.getUsageMsgNum() + 1);
        userChatLimit.setAllUsageMsgNum(userChatLimit.getAllUsageMsgNum() + 1);
        userChatLimit.setUpdateTime(new Date());

        return saveFlag ? save(userChatLimit):updateById(userChatLimit);
    }

    /**
     * @param uid
     * @return
     */
    @Override
    public boolean updateByCreateThread(Integer uid) {
        String day = DateUtil.format(new DateTime(),"yyyyMMdd");
        boolean saveFlag = false;
        UserChatLimitEntity userChatLimit = getByUid(uid);
        if (userChatLimit == null){
            userChatLimit = initLimit(uid,day);
            saveFlag = true;
        }
        if (!day.equals(userChatLimit.getLimitDay())){
            userChatLimit.setLimitDay(day);
            userChatLimit.setUsageMsgNum(0);
        }
        userChatLimit.setAllUsageThreadNum(userChatLimit.getAllUsageThreadNum() + 1);
        userChatLimit.setUpdateTime(new Date());

        return saveFlag ? save(userChatLimit):updateById(userChatLimit);
    }



    private UserChatLimitEntity initLimit(Integer uid,String limitDay){
        UserChatLimitEntity userChatLimit = new UserChatLimitEntity();
        userChatLimit.setUid(uid);
        userChatLimit.setLimitDay(limitDay);
        userChatLimit.setLimitMsgNum(ChatConstant.DEFAULT_CHAT_DAY_LIMIT);

        userChatLimit.setUsageMsgNum(0);
        userChatLimit.setAllUsageMsgNum(0);
        userChatLimit.setAllUsageThreadNum(1);
        userChatLimit.setAllUsageTokenNum(0);

        userChatLimit.setUpdateTime(new Date());
        userChatLimit.setCreateTime(new Date());

        return userChatLimit;
    }
}
