package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.viewmodel.ProgressView;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.eris.gitlabanalyzer.model.User;

@Service
public class MessageService {
    private final SimpMessageSendingOperations messagingTemplate;

    public MessageService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(ProgressView message, User user) {
        messagingTemplate.convertAndSend("/topic/progress/" + user.getId(), message);
    }
}
