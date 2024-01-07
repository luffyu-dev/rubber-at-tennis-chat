package com.rubber.at.tennis.chat.dao.dal;

import com.rubber.at.tennis.chat.dao.entity.UserChatThreadEntity;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

import java.util.List;

/**
 * <p>
 * 聊天线程记录信息 服务类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
public interface IUserChatThreadDal extends IBaseAdminService<UserChatThreadEntity> {


    List<UserChatThreadEntity> queryUserChatThread(Integer uid);


    UserChatThreadEntity getByThreadId(Integer uid,String threadId);
}
