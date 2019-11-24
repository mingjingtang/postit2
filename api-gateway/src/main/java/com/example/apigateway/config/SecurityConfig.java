package com.example.apigateway.config;

import com.example.apigateway.service.UserService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean("encoder")
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {

    //@formatter:off
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    configuration.setAllowedHeaders(Arrays.asList(
        "X-Requested-With", "Origin", "Content-Type",
        "Accept", "Authorization", "cache-control"));
  //@formatter:on
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable().authorizeRequests()
        .antMatchers("/user/signup", "/user/login", "/post/list").permitAll()
        .antMatchers("/post/**/comment").permitAll()
            .antMatchers("/user/v2/api-docs").permitAll()
            .antMatchers("/post/v2/api-docs").permitAll()
            .antMatchers("/comment/v2/api-docs").permitAll()
//                .antMatchers(HttpMethod.POST, "/user/signup").permitAll()
//                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/post/list").permitAll()
//        .anyRequest().permitAll()
        .antMatchers("/user/**", "/post/**", "comment/**").authenticated()
        .antMatchers("/user/role/**").hasRole("ADMIN")
        .and().httpBasic().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService);
  }
}