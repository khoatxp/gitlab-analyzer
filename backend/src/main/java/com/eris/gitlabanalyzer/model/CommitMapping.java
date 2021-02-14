package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CommitMapping")
@Table(name = "commit_mapping")
public class CommitMapping {

    @Id
    @SequenceGenerator(
            name = "commit_mapping_sequence",
            sequenceName = "commit_mapping_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_mapping_sequence"
    )
    @Column(
            name = "commit_mapping_id"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_member_id_fk"
            )
    )
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "commit_id",
            referencedColumnName = "commit_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_commit_id_fk"
            )
    )
    private Commit commit;

    public CommitMapping() {
    }

    public CommitMapping(Member member, Commit commit) {
        this.member = member;
        this.commit = commit;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    @Override
    public String toString() {
        return "CommitMapping{" +
                "id=" + id +
                ", member=" + member +
                ", commit=" + commit +
                '}';
    }
}
