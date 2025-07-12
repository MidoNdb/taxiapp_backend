package coursePro.mr.taxiApp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @SuppressWarnings("null")
//     @Override
// protected void doFilterInternal(HttpServletRequest request,
//                                 HttpServletResponse response,
//                                 FilterChain filterChain)
//                                 throws ServletException, IOException {

//     final String authHeader = request.getHeader("Authorization");

//     // if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//     //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//     //     response.setContentType("application/json");
//     //     response.getWriter().write("{\"error\": \"Token JWT manquant ou invalide\"}");
//     //     return;
//     // }
    
//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }
//     final String token = authHeader.substring(7);

//     try {
//         final String username = jwtService.extractUsername(token);

//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

//             if (!jwtService.isTokenValid(token, userDetails)) {
//                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                 response.setContentType("application/json");
//                 response.getWriter().write("{\"error\": \"Token JWT invalide ou expiré\"}");
//                 return;
//             }

//             UsernamePasswordAuthenticationToken authToken =
//                 new UsernamePasswordAuthenticationToken(
//                     userDetails, null, userDetails.getAuthorities());

//             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//             SecurityContextHolder.getContext().setAuthentication(authToken);
//         }

//         filterChain.doFilter(request, response);

//     } catch (Exception e) {
//         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//         response.setContentType("application/json");
//         response.getWriter().write("{\"error\": \"Erreur lors de l'authentification: " + e.getMessage() + "\"}");
//     }
// }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // String token = null;
        // String username = null;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                System.out.println("User not found: " + username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

