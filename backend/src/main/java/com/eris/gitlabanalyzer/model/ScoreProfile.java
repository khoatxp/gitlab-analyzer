package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "score_Profile")
public class ScoreProfile {

    @Id
    @SequenceGenerator(name = "score_profile_seq", sequenceName = "score_profile_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "score_profile_seq")
    @Column(name="id", nullable = false)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="line")
    private float line;

    @Column(name="delete")
    private float delete;

    @Column(name = "syntax")
    private float syntax;

    @Column(name = "comments")
    private float comments;

    @ElementCollection
    @MapKeyColumn(name="extension")
    @Column(name="weight")
    private Map<String, Float> extension = new HashMap<String, Float>();

    public ScoreProfile(){
    }

    public ScoreProfile(String name, float line, float delete, float syntax, float comments){
        this.name = name;
        this.line = line;
        this.delete = delete;
        this.syntax = syntax;
        this.comments = comments;
    }

    public ScoreProfile(String name, float line, float delete, float syntax, float comments, Map<String, Float> extensions){
        this.name = name;
        this.line = line;
        this.delete = delete;
        this.syntax = syntax;
        this.comments = comments;
        if(extensions != null){
            this.addExtension(extensions);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLine() {
        return line;
    }

    public void setLine(float line) {
        this.line = line;
    }

    public float getSyntax() {
        return syntax;
    }

    public void setSyntax(float syntax) {
        this.syntax = syntax;
    }

    public float getDelete() {
        return delete;
    }

    public void setDelete(float delete) {
        this.delete = delete;
    }

    public float getComments() {
        return comments;
    }

    public void setComments(float comments) {
        this.comments = comments;
    }

    public Map<String, Float> getExtension() {
        return extension;
    }


    public void addExtension( Map<String, Float> New ){

       extension.putAll(New);
    }

    public void deleteExtension(String file){
        if(extension.containsKey(file)) {
            extension.remove(file);
        }
    }

}
