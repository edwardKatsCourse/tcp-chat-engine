package com.company.server.service;

import com.company.common.ClientType;
import com.company.server.processor.UserChatThread;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class UserServiceImpl implements UserService {

    private static final Set<UserChatThread> initiallyConnected = new CopyOnWriteArraySet<>();
    private static final Map<String, Set<Map.Entry<ClientType, UserChatThread>>> fullyConnectedUsers = new ConcurrentHashMap<>();
    private static final Map<ClientType, Set<UserChatThread>> clientTypeUsers = new ConcurrentHashMap<>();

    @Override
    public void addUserConnection(UserChatThread userChatThread) {
        initiallyConnected.add(userChatThread);
    }

    @Override
    public void updateUserConnection(String username, UserChatThread userChatThread, ClientType clientType) {
        fullyConnectedUsers.compute(username, (key, value) -> {
            if (value == null) {
                value = new CopyOnWriteArraySet<>();
            }

            value.add(Map.entry(clientType, userChatThread));
            return value;
        });

        initiallyConnected.remove(userChatThread);
        clientTypeUsers.compute(clientType, (key, value) -> {
            if (value == null) {
                value = new CopyOnWriteArraySet<>();
            }

            value.add(userChatThread);
            return value;
        });
    }

    @Override
    public Set<UserChatThread> findAllReaderUsers() {
        return clientTypeUsers.getOrDefault(ClientType.READ, new CopyOnWriteArraySet<>());
    }

    @Override
    public void removeUser(String username) {
        var userConnections = fullyConnectedUsers.remove(username);
        userConnections
                .forEach(entry -> {
                    var clientTypeConnections = clientTypeUsers.get(entry.getKey());
                    clientTypeConnections.remove(entry.getValue());
                });

    }

    @Override
    public void removeConnection(UserChatThread userChatThread) {

        var userConnections = fullyConnectedUsers.getOrDefault(userChatThread.getUsername(), new HashSet<>());

        userConnections.stream()
                .filter(entry -> entry.getValue() == userChatThread)
                .findFirst()
                .ifPresent(userConnections::remove);

        clientTypeUsers
                .getOrDefault(userChatThread.getClientType(), new HashSet<>())
                .remove(userChatThread)
        ;
    }
}
