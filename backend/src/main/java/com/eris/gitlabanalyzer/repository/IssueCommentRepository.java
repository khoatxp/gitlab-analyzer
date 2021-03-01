package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.IssueComment;
import com.eris.gitlabanalyzer.model.MergeRequestComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCommentRepository extends JpaRepository<IssueComment, Long> {
    @Query("select i from IssueComment i where i.gitLabIssueNoteId = ?1 and i.issue.id = ?2")
    IssueComment findByIssueNoteIdAndIssueId(Long issueNoteId, Long issueId);
}
