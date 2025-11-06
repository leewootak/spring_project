package com.canesblack.spring_project1.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration // 해당 클래스의 기능을 빈으로 만들어 스프링 컨테이너에 넣기
@EnableWebSecurity // SpringSecurity를 사용하기 위한 어노테이션
public class SecurityConfig {
	@Bean
	// SpringSecurity 기능 사용 시 이 메소드 안에 내용 작성
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf 해킹 기법으로 부터 보호조치를 하는 코드 => 나중에 js에다 csrf 기능도 넣어놓을 것
		http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			// cors는 특정 서버로만 데이터를 넘길 수 있도록 설정
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// 세션 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers("/", "/loginPage", "logout", "/noticeCheckPage", "/register", "/menu/all")
					.permitAll().requestMatchers(HttpMethod.POST, "/login").permitAll()
					.requestMatchers("/resources/**", "/WEB-INF/**").permitAll()
					.requestMatchers("/noticerAdd", "noticeModifyPage").hasAnyAuthority("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.POST, "/menu/add").hasAnyAuthority("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.POST, "/menu/update").hasAnyAuthority("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.DELETE, "/menu/delete").hasAnyAuthority("ADMIN", "MANAGER")
					.anyRequest().authenticated())
			.formLogin(login -> login
					// url을 작성해서 로그인 페이지로 이동할 때
					.loginPage("/loginPage").loginProcessingUrl("/login").failureUrl("/loginPage?error=true")
					.usernameParameter("username").passwordParameter("password")
					.successHandler(authenticationSuccessHandler()).permitAll())
			.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // /logout URL을 통해 로그아웃 진행
					.logoutSuccessUrl("/") // 로그아웃 성공 후 이 url로 리다이렉팅
					.invalidateHttpSession(true) // 세션 무효화
					.deleteCookies("JSESSIONID") // 쿠키 삭제
					.permitAll());

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {

				// 로그인이 성공했을 때 특별 기능을 넣고싶을 때(세션, 권한)
				HttpSession session = request.getSession(); // 세션 기능을 가지고 온 것

				boolean isManager = authentication.getAuthorities().stream()
						.anyMatch(grantedAuthoirity -> grantedAuthoirity.getAuthority().equals("ADMIN")
								|| grantedAuthoirity.getAuthority().equals("MANAGER"));

				if (isManager) {
					session.setAttribute("MANAGER", true);
				}

				session.setAttribute("username", authentication.getName());
				session.setAttribute("isAuthentication", true);

				// request.getContextPath() = localhost:8080
				response.sendRedirect(request.getContextPath() + "/");

				super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// localhost:8080 서버에서는 프론트와 백 사이에 데이터를 주고 받을 수 있게 만듦
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "https://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
