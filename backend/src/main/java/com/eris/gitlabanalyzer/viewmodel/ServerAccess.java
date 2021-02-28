package com.eris.gitlabanalyzer.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerAccess {
    private String serverUrl;
    private String accessToken;
    private String serverNickname;
}
