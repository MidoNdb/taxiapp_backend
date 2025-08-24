package coursePro.mr.taxiApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                // ✅ SOLUTION: Exclure complètement WebSocket de Spring Security
                .requestMatchers("/ws", "/ws/**").permitAll()
                .requestMatchers("/courses/admin/**").hasRole("ADMIN")
                .requestMatchers("/passagers/admin/**").hasRole("ADMIN")
                .requestMatchers("/conducteurs/admin/**").hasRole("ADMIN") 
                .requestMatchers("/transactions/admin/**").hasRole("ADMIN")
                .requestMatchers("/transactions/uploads/**").hasRole("ADMIN")
                .requestMatchers("/payment-numbers/admin/**").hasRole("ADMIN")
                
                .requestMatchers("/courses/passager/**").hasRole("PASSAGER")
                .requestMatchers("/passagers/passager/**").hasRole("PASSAGER")

                .requestMatchers("/courses/conducteur/**").hasRole("CONDUCTEUR")
                .requestMatchers("/conducteurs/conducteur/**").hasRole("CONDUCTEUR")
                .requestMatchers("/wallet/conducteur/**").hasRole("CONDUCTEUR")
                 
                .requestMatchers("/transactions/conducteur/**").hasRole("CONDUCTEUR")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Solution plus compatible pour éviter les conflits
        configuration.setAllowedOrigins(Arrays.asList("*"));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Pas de credentials avec wildcard origin
        configuration.setAllowCredentials(false);
        
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// package coursePro.mr.taxiApp.security;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.Arrays;
// import java.util.List;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;
//     private final UserDetailsServiceImpl userDetailsService;

//     @Autowired
//     public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
//         this.jwtAuthFilter = jwtAuthFilter;
//         this.userDetailsService = userDetailsService;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ✅ Autoriser OPTIONS
//                 .requestMatchers("/auth/**").permitAll()
//                 .requestMatchers("/ws/**").permitAll() // ✅ WebSocket endpoints
//                 .requestMatchers("/courses/admin/**").hasRole("ADMIN")
//                 .requestMatchers("/passagers/admin/**").hasRole("ADMIN")
//                 .requestMatchers("/conducteurs/admin/**").hasRole("ADMIN") 
//                 .requestMatchers("/transactions/admin/**").hasRole("ADMIN")
//                 .requestMatchers("/transactions/uploads/**").hasRole("ADMIN")
//                 .requestMatchers("/payment-numbers/admin/**").hasRole("ADMIN")
                
//                 .requestMatchers("/courses/passager/**").hasRole("PASSAGER")
//                 .requestMatchers("/passagers/passager/**").hasRole("PASSAGER")

//                 .requestMatchers("/courses/conducteur/**").hasRole("CONDUCTEUR")
//                 .requestMatchers("/conducteurs/conducteur/**").hasRole("CONDUCTEUR")
//                 .requestMatchers("/wallet/conducteur/**").hasRole("CONDUCTEUR")
                 
//                 .requestMatchers("/transactions/conducteur/**").hasRole("CONDUCTEUR")
//                 .anyRequest().authenticated()
//             )
//             .authenticationProvider(authenticationProvider())
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     // ✅ CORS Configuration CORRIGÉE pour la production
//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
        
//         // ✅ SOLUTION 1 : Autoriser tous les domaines temporairement pour tester
//         configuration.setAllowedOriginPatterns(List.of("*"));
        
//         // ✅ SOLUTION 2 (RECOMMANDÉE) : Spécifier vos domaines autorisés
//         // configuration.setAllowedOrigins(List.of(
//         //     "http://localhost:3000",           // Développement local
//         //     "https://your-frontend-domain.com", // Production frontend
//         //     "https://your-admin-panel.com"     // Admin panel si différent
//         // ));
        
//         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//         configuration.setAllowedHeaders(List.of("*"));
        
//         // ✅ Permettre les credentials si nécessaire
//         configuration.setAllowCredentials(true);
        
//         // ✅ Exposer les headers nécessaires
//         configuration.setExposedHeaders(List.of("Authorization"));
        
//         // ✅ Cache preflight requests
//         configuration.setMaxAge(3600L);
        
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }

//     @Bean
//     public AuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         provider.setUserDetailsService(userDetailsService);
//         provider.setPasswordEncoder(passwordEncoder());
//         return provider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }