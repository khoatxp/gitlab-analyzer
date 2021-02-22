package com.eris.gitlabanalyzer.model.gitlabresponse;

import lombok.*;

@Data
@NoArgsConstructor
public class GitLabMember {
    private Long id;
    private String username;
    private String name;
}
