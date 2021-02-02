package com.eris.gitlabanalyzer.model;
import javax.persistence.*;
@Entity
@Table
public class Project {
    @Id
    private Long id;
    private String name;
    private String nameWithNamespace;
    private String web_url;

    public Project(Long id, String name, String nameWithNamespace, String web_url) {
        this.id = id;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.web_url = web_url;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameWithNamespace='" + nameWithNamespace + '\'' +
                ", web_url='" + web_url + '\'' +
                '}';
    }
}
