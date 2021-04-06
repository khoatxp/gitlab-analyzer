/*
		Gitlab Analyzer
		Copyright (C) <2021>  <Andrew Turner, Andrey Pham, Asim Shrestha, Ida Nakhostin, Jason Lee, Kemo Sonko, Mahek Khan, Michael Zheng>

		This program is free software: you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation, either version 3 of the License, or
		(at your option) any later version.

		This program is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.eris.gitlabanalyzer;
import com.eris.gitlabanalyzer.service.AuthService;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;

@SpringBootApplication
public class GitLabAnalyzerApplication {

	@Value("${SFU_CAS_URL}")
	String sfuCasUrl;

	@Value("${BACKEND_URL}")
	String backendUrl;

	public static void main(String[] args) {
		SpringApplication.run(GitLabAnalyzerApplication.class, args);
	}

	// CAS Setup Based on https://github.com/eugenp/tutorials/tree/master/cas/cas-secured-app
	@Bean
	public CasAuthenticationFilter casAuthenticationFilter(
			AuthenticationManager authenticationManager,
			ServiceProperties serviceProperties) throws Exception {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setAuthenticationSuccessHandler((request,response,authentication) -> {
			response.sendRedirect("/");
		});
		filter.setAuthenticationFailureHandler((request, response, authenticationException) -> {
			System.out.println("Auth failure: ");
		System.out.println(authenticationException);
		response.sendError(401);
		});
		filter.setAuthenticationManager(authenticationManager);
		filter.setServiceProperties(serviceProperties);
		return filter;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(backendUrl + "/login/cas");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public TicketValidator ticketValidator() {
		return new Cas30ServiceTicketValidator(sfuCasUrl);
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider(
			TicketValidator ticketValidator,
			ServiceProperties serviceProperties, AuthService authService) {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties);
		provider.setTicketValidator(ticketValidator);
		provider.setUserDetailsService(authService);
		provider.setKey("CAS_PROVIDER_SFU");
		return provider;
	}
}
