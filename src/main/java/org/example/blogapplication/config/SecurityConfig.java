
package org.example.blogapplication.config;

import org.example.blogapplication.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


@Configuration
@EnableMethodSecurity // Enables @PreAuthorize and similar annotations
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // pour H2 et formulaire simple
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/web/users/create", // S'inscrire
                                "/auth",   // Page d'accueil avec les boutons
                                "/h2-console/**",    // Pour accéder H2 console sans problème
                                "/login"             // Permettre l'accès au formulaire login
                        ).permitAll()
                        // 👇 Admin-only actions
                        .requestMatchers("/admin/**", "/web/users/delete/**").hasRole("ADMIN")

                        // 👇 Authenticated users can access dashboard and post actions
                        .requestMatchers("/dashboard", "/posts/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // c'est "/login" ton formulaire de connexion
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true") // rediriger proprement en cas d'erreur
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 👈 after logout go to /login
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // pour H2 console

        return http.build();
    }


}




