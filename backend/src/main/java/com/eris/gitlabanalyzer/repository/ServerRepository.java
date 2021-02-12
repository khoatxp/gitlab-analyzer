package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Server;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServerRepository extends PagingAndSortingRepository<Server, String> {
}
