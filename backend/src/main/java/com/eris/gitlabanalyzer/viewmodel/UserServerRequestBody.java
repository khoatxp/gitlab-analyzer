package com.eris.gitlabanalyzer.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserServerRequestBody {
    private String serverUrl;
    private String accessToken;
}
