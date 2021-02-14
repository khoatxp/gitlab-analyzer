package com.eris.gitlabanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModelTests {


    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Project testProject = new Project(2L, "Test123", "TestingTheThing", "url");

    @Test
    void projectModel() {
        projectRepository.save(testProject);
        Project r = projectRepository.findById(2L).orElse(null);
        assertNotNull(r);
        assertEquals(2L, r.getId());
        assertEquals("Test123", r.getName());
        assertEquals("TestingTheThing", r.getNameWithNamespace());
        assertEquals("url", r.getWebUrl());
        assertEquals("", r.getServerUrl());
    }
    @Test
    void memberModel() {
        memberRepository.save(new Member(1L, "John123","John",testProject ));
        Member m = memberRepository.findById(1L).orElse(null);
        assertEquals(1L, m.getId());
        assertEquals("John123", m.getUsername());
        assertEquals("John", m.getName());
    }
}
