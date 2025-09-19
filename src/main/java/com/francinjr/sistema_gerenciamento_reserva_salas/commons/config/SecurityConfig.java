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

    // Injeta o nosso handler de sucesso de autenticação customizado
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

                        // Permite todos os encaminhamentos internos (FORWARD) para evitar o loop de redirecionamento com JSP
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                        // Libera as URLs públicas que qualquer um pode acessar
                        .requestMatchers(
                                "/login",
                                "/clientes/novo",
                                "/clientes",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // ✅ NOVA REGRA: Protege o painel da recepcionista
                        .requestMatchers("/painel-recepcionista/**").hasAnyRole("RECEPCIONISTA", "ADMINISTRADOR")

                        // Protege as rotas administrativas
                        .requestMatchers("/setores/**", "/salas/**", "/recepcionistas/**").hasRole("ADMINISTRADOR")

                        // Qualquer outra requisição exige que o usuário esteja autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        // Usa o nosso handler customizado para decidir para onde redirecionar após o login
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }
}