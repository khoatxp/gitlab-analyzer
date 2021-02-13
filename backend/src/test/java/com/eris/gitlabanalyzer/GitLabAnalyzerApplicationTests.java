package com.eris.gitlabanalyzer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GitLabAnalyzerApplicationTests {

	@Autowired
	private GitLabAnalyzerApplication analyzer;

	@Test
	void contextLoads() {
		assertThat(analyzer).isNotNull();
	}

}
