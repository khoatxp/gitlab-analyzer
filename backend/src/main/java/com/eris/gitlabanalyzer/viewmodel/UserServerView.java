package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.UserServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserServerView {
    private Long serverId;
    private String serverUrl;

    public static UserServerView fromUserServer(UserServer userServer) {
        var server = userServer.getServer();
        return new UserServerView(server.getId(), server.getServerUrl());
    }
}
