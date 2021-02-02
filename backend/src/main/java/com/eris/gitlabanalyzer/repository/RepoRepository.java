package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long> {

}
