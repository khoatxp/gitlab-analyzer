package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.MergeRequestComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRequestCommentRepository extends JpaRepository<MergeRequestComment, Long> {
    @Query("select m from MergeRequestComment m where m.gitLabMergeRequestNoteId = ?1 and m.mergeRequest.id = ?2")
    MergeRequestComment findByGitLabMergeRequestNoteIdAndMergeRequestId(Long gitLabMergeRequestNoteId, Long mergeRequestId);
}
