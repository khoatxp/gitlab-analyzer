package com.eris.gitlabanalyzer.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {
    private final RepoRepository repoRepository;

    @Autowired
    public RepoService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public List<Repo> getRepos() {
        return repoRepository.findAll();
    }

    public void addNewRepo(Repo repo) {
        repoRepository.save(repo);
    }
}
