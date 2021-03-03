package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Score_Profile")
public class ScoreProfile {

    @Id
    @SequenceGenerator(name = "score_profile_seq", sequenceName = "score_profile_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "score_profile_seq")
    @Column(name="id", nullable = false)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="line")
    private double lineWeight;

    @Column(name="delete")
    private double deleteWeight;

    @Column(name = "syntax")
    private double syntaxWeight;

    @Column(name = "comments")
    private double commentsWeight;

    @ElementCollection
    @MapKeyColumn(name="extension")
    @Column(name="weight")
    private Map<String, Double> extensionWeights = new HashMap<String, Double>();

    public ScoreProfile(){
    }

    public ScoreProfile(String name, double lineWeight, double deleteWeight, double syntaxWeight, double commentsWeight){
        this.name = name;
        this.lineWeight = lineWeight;
        this.deleteWeight = deleteWeight;
        this.syntaxWeight = syntaxWeight;
        this.commentsWeight = commentsWeight;
    }

    public ScoreProfile(String name, double lineWeight, double deleteWeight, double syntaxWeight, double commentsWeight, Map<String, Double> extensions){
        this.name = name;
        this.lineWeight = lineWeight;
        this.deleteWeight = deleteWeight;
        this.syntaxWeight = syntaxWeight;
        this.commentsWeight = commentsWeight;
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

    public double getLineWeight() {
        return lineWeight;
    }

    public void setLineWeight(double line) {
        this.lineWeight = line;
    }

    public double getSyntaxWeight() {
        return syntaxWeight;
    }

    public void setSyntaxWeight(double syntaxWeight) {
        this.syntaxWeight = syntaxWeight;
    }

    public double getDeleteWeight() {
        return deleteWeight;
    }

    public void setDeleteWeight(double delete) {
        this.deleteWeight = delete;
    }

    public double getCommentsWeight() {
        return commentsWeight;
    }

    public void setCommentsWeight(double comments) {
        this.commentsWeight = comments;
    }

    public Map<String, Double> getExtension() {
        return extensionWeights;
    }


    public void addExtension( Map<String, Double> New ){

       extensionWeights.putAll(New);
    }

    public void deleteExtension(String file){
        if(extensionWeights.containsKey(file)) {
            extensionWeights.remove(file);
        }
    }

}
