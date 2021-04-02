package com.eris.gitlabanalyzer.config;

import com.eris.gitlabanalyzer.interceptor.GitLabServiceConfigInterceptor;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GitLabServiceConfig implements WebMvcConfigurer {

    @Bean
    @RequestScope
    public GitLabService requestScopeGitLabService() {
        return new GitLabService();
    }

    @Bean
    public GitLabServiceConfigInterceptor securityInterceptor()
    {
        return new GitLabServiceConfigInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor()).addPathPatterns("/api/**");
    }

}
