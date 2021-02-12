package com.eris.gitlabanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DBSampleTests {


    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void checkRepo_Save() {

        projectRepository.save(new Project(2L, "Test123", "TestingTheThing", "url"));
        Project r = projectRepository.findById(2L).orElse(null);
        assertNotNull(r);
        assertEquals("TestingTheThing", r.getNameWithNamespace());


    }

}
