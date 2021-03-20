package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalyticsProgress;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.eris.gitlabanalyzer.model.User;

@Service
public class MessageService {
    private final SimpMessageSendingOperations messagingTemplate;

    public MessageService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(AnalyticsProgress message, User user) {
        messagingTemplate.convertAndSend("/topic/progress/" + user.getId(), message);
    }
}
