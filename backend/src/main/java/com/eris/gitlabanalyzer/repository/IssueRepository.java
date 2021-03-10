package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("select i from Issue i where i.iid = ?1 and i.project.id = ?2")
    Issue findByIidAndProjectId(Long iid, Long projectId);
}
