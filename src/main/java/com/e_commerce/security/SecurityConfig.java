package com.e_commerce.security;

import com.e_commerce.service.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailServiceImpl;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtRequestFilter jwtRequestFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity; enable in production
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS

                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to the auth routes
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Allow only GET requests to product routes
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll() // Allow GET requests for products
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN") // Allow POST by admin only
                         .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN") // Allow PUT by admin only
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN") // Allow DELETE by admin only
                        .requestMatchers("/api/v1/products/**").permitAll() // Allow GET requests for products
                        // Allow only authenticated users to cart and order routes
                        .requestMatchers("/api/v1/cart/**").authenticated()
                        .requestMatchers("/api/v1/order/**").authenticated()

                        // Allow only admins to categories
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll() // Allow GET requests for products
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN") // Allow POST by admin only
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN") // Allow PUT by admin only
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN") // Allow DELETE by admin only

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://fashion-fusion-ff.vercel.app")); // Allow frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return  authConfig.getAuthenticationManager();

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();

    }

}
