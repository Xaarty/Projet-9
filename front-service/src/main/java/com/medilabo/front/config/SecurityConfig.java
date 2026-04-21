package com.medilabo.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Autorise l’accès au style, le logo et à la page de login
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        // Ou les autres requêtes nécessitant une authentification
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Redirection vers la liste des patients après connexion
                        .defaultSuccessUrl("/patients", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        // Redirection après déconnexion
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // Utilisateur en mémoire utilisé pour l’accès au front
        return new InMemoryUserDetailsManager(
                User.withUsername("doctor")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Encodage sécurisé du mot de passe
        return new BCryptPasswordEncoder();
    }
}