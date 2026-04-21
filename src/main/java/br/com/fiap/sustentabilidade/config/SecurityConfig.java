package br.com.fiap.sustentabilidade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // usuários de teste (memória) — para Insomnia/Postman
    @Bean
    public UserDetailsService users() {
        // {noop} = sem hash (apenas para testes). Depois troque por BCrypt.
        var admin = User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build();
        var user  = User.withUsername("user").password("{noop}user123").roles("USER").build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API REST
                .authorizeHttpRequests(auth -> auth
                        // público (se quiser expor um healthcheck)
                        .requestMatchers("/saude", "/actuator/health").permitAll()

                        // leitura (GET) liberada para USER e ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER","ADMIN")

                        // escrita restrita
                        .requestMatchers(HttpMethod.POST,   "/api/equipamentos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/setores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/equipamentos/**").hasRole("ADMIN")

                        // qualquer outra rota precisa estar autenticada
                        .anyRequest().authenticated()
                )
                // HTTP Basic (o que a FIAP usa nos exemplos iniciais)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
