package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p from Project p where p.gitLabProjectId = ?1 and p.server.serverUrl = ?2")
    List<Member> findByGitlabProjectId(Long gitLabProjectId, String serverUrl);
}
