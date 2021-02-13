package com.eris.gitlabanalyzer.model;

import lombok.*;

@Data
@NoArgsConstructor
public class GitLabMember {
    private Long id;
    private String username;
    private String name;
}
