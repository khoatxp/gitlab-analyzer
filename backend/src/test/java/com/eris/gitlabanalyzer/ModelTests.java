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
    private Member testmember = new Member(1L, "John123","John",testProject );

    @Test
    void projectModel() {
        projectRepository.save(testProject);
        Project r = projectRepository.findById(2L).orElse(null);
        assertNotNull(r);
        assertEquals(testProject.getId(), r.getId());
        assertEquals(testProject.getName(), r.getName());
        assertEquals(testProject.getNameWithNamespace(), r.getNameWithNamespace());
        assertEquals(testProject.getWebUrl(), r.getWebUrl());
        assertEquals(testProject.getServerUrl(), r.getServerUrl());
    }
    @Test
    void memberModel() {
        memberRepository.save(testmember);
        Member m = memberRepository.findById(1L).orElse(null);
        assertEquals(testmember.getId(), m.getId());
        assertEquals(testmember.getUsername(), m.getUsername());
        assertEquals(testmember.getName(), m.getName());
    }
}
