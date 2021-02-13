package com.eris.gitlabanalyzer.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
public class Project {
    @Id
    private Long id;

    private String name;

    private String nameWithNamespace;

    private String webUrl;

    @Transient
    private String serverUrl;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Member> members = new HashSet<>();

    public Project(Long id, String name, String nameWithNamespace, String webUrl) {
        this.id = id;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.webUrl = webUrl;
        this.serverUrl = createServerUrl();
    }

    public String createServerUrl() {
        try {
            URL webUrl = new URL(this.webUrl);
            return webUrl.getProtocol() + "://" + webUrl.getHost();
        } catch (MalformedURLException e) {
            return "";
        }
    }
}
