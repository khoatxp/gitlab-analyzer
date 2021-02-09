package com.eris.gitlabanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import com.eris.gitlabanalyzer.repo.Repo;
import com.eris.gitlabanalyzer.repo.RepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DBSampleTests {


    @Autowired
    private RepoRepository repoRepository;

    @Test
    void checkRepo_Save() {

        repoRepository.save(new Repo( "Test123"));
        Repo r = repoRepository.findById(2L).orElse(null);
        assertNotNull(r);
        assertEquals("Test123", r.getNamespace());


    }

}
