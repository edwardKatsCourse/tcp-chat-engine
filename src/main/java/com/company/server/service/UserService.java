package com.company.server.service;

import com.company.common.ClientType;
import com.company.server.processor.UserChatThread;

import java.util.Set;

public interface UserService {


    void addUserConnection(UserChatThread userChatThread);
    void updateUserConnection(String username, UserChatThread userChatThread, ClientType clientType);

    Set<UserChatThread> findAllReaderUsers();
    void removeUser(String username);
    void removeConnection(UserChatThread userChatThread);

}
