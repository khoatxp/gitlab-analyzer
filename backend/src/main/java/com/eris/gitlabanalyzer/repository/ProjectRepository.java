package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // TODO use serverId instead of serverUrl
    @Query("select p from Project p where p.gitLabProjectId = ?1 and p.server.serverUrl = ?2")
    Project findByGitlabProjectIdAndServerUrl(Long gitLabProjectId, String serverUrl);
}
