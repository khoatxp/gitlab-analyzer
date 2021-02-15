package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {

}
