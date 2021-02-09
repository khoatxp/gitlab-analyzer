package com.eris.gitlabanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import com.eris.gitlabanalyzer.repo.Project;
import com.eris.gitlabanalyzer.repository.projectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DBSampleTests {


    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void checkRepo_Save() {

        projectRepository.save(new Repo( "Test123"));
        Project r = projectRepository.findById(2L).orElse(null);
        assertNotNull(r);
        assertEquals("Test123", r.getNamespace());


    }

}
