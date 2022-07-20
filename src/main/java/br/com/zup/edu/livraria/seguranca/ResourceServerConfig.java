package br.com.zup.edu.livraria.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .csrf().disable()
                    .httpBasic().disable()
                    .rememberMe().disable()
                    .formLogin().disable()
                    .logout().disable()
                    .requestCache().disable()
                    .headers().frameOptions().deny()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/livros").hasAuthority("SCOPE_livros:write")
                        .antMatchers(HttpMethod.GET, "/api/livros/**").hasAuthority("SCOPE_livros:read")
                        .antMatchers(HttpMethod.POST, "/api/autores").hasAuthority("SCOPE_autores:write")
                        .antMatchers(HttpMethod.DELETE, "/api/autores/**").hasAuthority("SCOPE_autores:write")
                        .antMatchers(HttpMethod.GET, "/api/autores/**").hasAuthority("SCOPE_autores:read")
                    .anyRequest()
                        .authenticated()
                .and()
                    .oauth2ResourceServer()
                        .jwt()
        ;
    }

}
