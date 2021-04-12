package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Score_Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreProfile {

    @Id
    @SequenceGenerator(name = "score_profile_seq", sequenceName = "score_profile_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "score_profile_seq")
    @Column(name="id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id")
    private User user;

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

    @ElementCollection
    @Column(name="blackList")
    private List<String> blackList = new ArrayList<>();

    public ScoreProfile(String name, double lineWeight, double deleteWeight, double syntaxWeight, double commentsWeight){
        this.name = name;
        this.lineWeight = lineWeight;
        this.deleteWeight = deleteWeight;
        this.syntaxWeight = syntaxWeight;
        this.commentsWeight = commentsWeight;
    }

    public void addExtension( Map<String, Double> newExtension ){
       extensionWeights.putAll(newExtension);
    }

    public void deleteExtension(String file){
        if(extensionWeights.containsKey(file)) {
            extensionWeights.remove(file);
        }
    }

    public boolean ExtensionContains(String extension){
        return extensionWeights.containsKey(extension);
    }

    public double getExtensionWeight(String extension){
        return extensionWeights.get(extension);
    }

    public void addBlackList(List<String> newBlackList){
        blackList.addAll(newBlackList);
    }


    public void deleteFromBlackList(String extension){
        if (blackList.contains(extension)){
            blackList.remove(extension);
        }
    }

    public boolean blackListContains(String extension){
        return blackList.contains(extension);
    }


}
