package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueCommentRepository extends JpaRepository<Note, Long> {
    @Query("select n from Note n where n.projectId = ?1")
    List<Note> findAllByProjectId(Long projectId);

    @Query("select n from Note n where n.gitLabNoteId = ?1 and n.projectId = ?2")
    Optional<Note> findByGitLabNoteIdAndProjectId(Long gitLabNoteId, Long projectId);
}
