package com.bank.atm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public InMemoryUserDetailsManager userDetailService() {
		UserDetails guestUser = User.withUsername("guest").password(passwordEncoder().encode("guest")).roles("User").build();
		UserDetails adminUser = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("Admin").build();
		return new InMemoryUserDetailsManager(guestUser, adminUser);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.httpBasic(Customizer.withDefaults())
		.authorizeHttpRequests(
				requests -> requests.requestMatchers(HttpMethod.POST, "/atm/v1/reloadBalance").hasRole("Admin")
				.requestMatchers(HttpMethod.GET, "/atm/v1/currentBalance").hasRole("Admin")
				.anyRequest().authenticated()
				).csrf(csrf -> csrf.disable());
		return http.build();
	}
}
