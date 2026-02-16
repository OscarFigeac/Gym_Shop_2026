package org.example.gym_shop_2026.configs;

import org.example.gym_shop_2026.persistence.UserDAO;
import org.example.gym_shop_2026.security.CustomAuthenticationDetailsSource;
import org.example.gym_shop_2026.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.SQLException;

import static org.springframework.security.config.Elements.CSRF;

/**
 * Configuration class for Spring Security.
 * @author Oscar Figeac & Cal Woods
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;

    private final CustomAuthenticationProvider authProvider;
    private final CustomAuthenticationDetailsSource detailsSource;
    private final UserDAO userDAO;

    @Autowired
    public SecurityConfig(CustomAuthenticationProvider authProvider,
                          CustomAuthenticationDetailsSource detailsSource,
                          UserDAO userDAO){
        this.authProvider = authProvider;
        this.detailsSource = detailsSource;
        this.userDAO = userDAO;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try{
                org.example.gym_shop_2026.entities.User user = userDAO.findByUsername(username);
                if (user == null){
                    throw new UsernameNotFoundException("User not found: " + username);
                }

                // returns the spring security user object mapped from our database user
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .disabled(false)
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .accountLocked(false)
                        .authorities("USER")
                        .build();
            } catch (SQLException e){
                throw new UsernameNotFoundException("Database error during authentication", e);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //enable this for production, testing purposes only for disabling it
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/confirm-2fa", "/setup-2fa", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .authenticationDetailsSource(detailsSource)
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.authenticationProvider(authProvider);

        return http.build();
    }
}
