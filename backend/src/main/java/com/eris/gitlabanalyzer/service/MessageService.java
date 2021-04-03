package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageService {
    private final SimpMessageSendingOperations messagingTemplate;
    private final AnalysisRunRepository analysisRunRepository;

    public MessageService(SimpMessageSendingOperations messagingTemplate, AnalysisRunRepository analysisRunRepository) {
        this.messagingTemplate = messagingTemplate;
        this.analysisRunRepository = analysisRunRepository;
    }

    public void sendMessage(AnalysisRun analysisRun) {
        messagingTemplate.convertAndSend("/topic/progress/" + analysisRun.getId(), AnalysisRunView.progressFromAnalysisRun(analysisRun));
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        String channel = event.getMessage().getHeaders().get("simpDestination").toString();
        //Parse channel string to get analysisRunId
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(channel);
        if(matcher.find()){
            Long analysisRunId = Long.parseLong(matcher.group());
            AnalysisRun analysisRun = analysisRunRepository.findById(analysisRunId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to fetch analysis run progress"));
            messagingTemplate.convertAndSend(channel, AnalysisRunView.progressFromAnalysisRun(analysisRun));
        }
    }
}
