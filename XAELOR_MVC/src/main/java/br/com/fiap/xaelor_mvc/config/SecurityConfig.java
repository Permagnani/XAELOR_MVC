package br.com.fiap.xaelor_mvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação Xaelor MVC.
 *
 * Rotas PÚBLICAS: página inicial, listagem e detalhe de perfumes/matérias-primas (READ),
 *                  recursos estáticos (css/js/img) e a página de login.
 * Rotas PRIVADAS: qualquer operação de escrita (CREATE, UPDATE, DELETE) - exige login.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.admin-user}")
    private String adminUser;

    @Value("${app.security.admin-password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username(adminUser)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // recursos estáticos e páginas públicas de leitura
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("GET", "/perfumes", "/perfumes/{id:[0-9]+}").permitAll()
                .requestMatchers("GET", "/materias-primas", "/materias-primas/{id:[0-9]+}").permitAll()
                // qualquer outra rota (formulários de criar/editar/excluir) exige login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", false)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // necessário para o console do H2 funcionar dentro de um frame (perfil h2)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
}
