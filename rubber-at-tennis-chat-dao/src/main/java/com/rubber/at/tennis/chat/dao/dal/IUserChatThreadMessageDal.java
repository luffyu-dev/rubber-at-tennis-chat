package com.rubber.at.tennis.chat.dao.dal;

import com.rubber.at.tennis.chat.dao.entity.UserChatThreadMessageEntity;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

import java.util.List;

/**
 * <p>
 * 聊天内容 服务类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-21
 */
public interface IUserChatThreadMessageDal extends IBaseAdminService<UserChatThreadMessageEntity> {



    UserChatThreadMessageEntity queryMsgStatus(Integer uid,String threadId,String msgId);




    UserChatThreadMessageEntity saveOrUpdateByEntity(UserChatThreadMessageEntity userChatThreadMessageEntity);
}
