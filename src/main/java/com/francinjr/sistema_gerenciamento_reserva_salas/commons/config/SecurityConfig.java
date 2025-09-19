package com.francinjr.sistema_gerenciamento_reserva_salas.commons.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers(
                                "/login", "/clientes/novo", "/clientes", "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        // Regras de autorização por papel para cada funcionalidade
                        .requestMatchers("/painel-recepcionista/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/setores/**", "/salas/**", "/recepcionistas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/", "/agendamentos/solicitar/**", "/agendamentos/cancelar/**").hasRole("CLIENTE")
                        .requestMatchers("/historico/**", "/relatorios/**").authenticated() // Todos logados podem ver relatórios
                        .requestMatchers("/inicio").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }
}