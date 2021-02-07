package com.eris.gitlabanalyzer.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/repo")
public class RepoController {
    private final RepoService repoService;

    @Autowired
    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping
    public List<Repo> getRepos() {
        return repoService.getRepos();
    }

    @PostMapping
    public void addNewRepo(@RequestBody  Repo repo) {
        repoService.addNewRepo(repo);
    }
}
