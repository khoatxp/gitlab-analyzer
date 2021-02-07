package com.eris.gitlabanalyzer.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "project")
public class Project {
    @Id
    @Column(name = "id")
    private Long id;

    private String name;

    @JsonProperty("name_with_namespace")
    private String nameWithNamespace;

    @JsonProperty("web_url")
    private String webUrl;

    @Transient
    private String serverUrl;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Member> members = new HashSet<>();

    public Project(){}

    public Project(Long id, String name, String nameWithNamespace, String webUrl, String serverUrl) {
        this.id = id;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.webUrl = webUrl;
        this.serverUrl = serverUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getServerUrl() {
        try {
            URL webUrl = new URL(this.webUrl);
            return webUrl.getProtocol() + "://" + webUrl.getHost();
        } catch (MalformedURLException e) {
            return "";
        }

    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameWithNamespace='" + nameWithNamespace + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", serverUrl='" + serverUrl + '\'' +

                '}';
    }
}
