package com.eris.gitlabanalyzer.interceptor;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.ProjectService;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class GitLabServiceConfigInterceptor implements HandlerInterceptor {

    @Autowired
    private GitLabService gitLabService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserServerService userServerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AccessDeniedException {
        var principal = request.getUserPrincipal();
        if (principal == null) {
            throw new AccessDeniedException("User not logged in.");
        }
        User user = authService.getLoggedInUser(principal);

        var pathParameters = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathParameters != null && pathParameters.containsKey("serverId")) {
            Long serverId = Long.parseLong(pathParameters.get("serverId"));
            var userServer = userServerService.getUserServer(user, serverId).orElseThrow(
                    () -> new AccessDeniedException("User does not have server permission.")
            );
            var server = serverRepository.findById(serverId).get();
            gitLabService.setServerUrl(server.getServerUrl());
            gitLabService.setAccessToken(userServer.getAccessToken());
        }
        else if (pathParameters != null && pathParameters.containsKey("projectId")) {
            Long projectId = Long.parseLong(pathParameters.get("projectId"));
            var project = projectService.getProjectById(projectId);
            var server = project.getServer();

            if (!authService.hasProjectPermission(user.getId(), server.getId(), projectId)) {
                throw new AccessDeniedException("User does not have project permission.");
            }
            var userServer = userServerService.getUserServer(user, server.getId()).orElseThrow(
                    () -> new AccessDeniedException("User does not have server permission.")
            );
            gitLabService.setServerUrl(server.getServerUrl());
            gitLabService.setAccessToken(userServer.getAccessToken());
        }
        return true;
    }
}
