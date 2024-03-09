package com.rubber.at.tennis.chat.dao.dal;

import com.rubber.at.tennis.chat.dao.entity.UserChatLimitEntity;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

/**
 * <p>
 * chat聊天限制数 服务类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
public interface IUserChatLimitDal extends IBaseAdminService<UserChatLimitEntity> {


    UserChatLimitEntity getByUid(Integer uid);


    boolean updateBySendMsg(Integer uid);


    boolean updateByCreateThread(Integer uid);


}
