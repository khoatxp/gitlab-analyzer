package com.eris.gitlabanalyzer.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Value("${SFU_CAS_URL}")
    String sfuCasUrl;

    private SingleSignOutFilter singleSignOutFilter;
    private LogoutFilter logoutFilter;
    private CasAuthenticationProvider casAuthenticationProvider;
    private ServiceProperties serviceProperties;

    @Autowired
    public ApplicationSecurityConfig(CasAuthenticationProvider casAuthenticationProvider,
                                     ServiceProperties serviceProperties) {
        this.casAuthenticationProvider = casAuthenticationProvider;
        this.serviceProperties = serviceProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configures app to require authentication for every request
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated()
                .and()
                // if uncommented, auto-redirect to CAS login if accessing an authenticated
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint())
                .csrf()
                .disable() // TODO: This will allow POST and DELETE requests to the server but left it vulnerable to CSRF attacks. Should have proper csrf configuration later on
                .logout(logout -> logout
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                )
        ;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList(FRONTEND_URL));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider);
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(casAuthenticationProvider));
    }


    public AuthenticationEntryPoint authenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(sfuCasUrl + "/login");
        entryPoint.setServiceProperties(serviceProperties);
        return entryPoint;
    }
}
