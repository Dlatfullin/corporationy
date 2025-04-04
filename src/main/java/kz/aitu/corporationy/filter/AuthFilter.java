package kz.aitu.corporationy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.aitu.corporationy.service.UserService;
import kz.aitu.corporationy.util.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request,
                                    final @NonNull HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        TokenUtils.extractTokenFromHeaders(request)
                .filter(token -> SecurityContextHolder.getContext().getAuthentication() == null)
                .flatMap(userService::getByToken)
                .ifPresent(user -> {
                    var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
        filterChain.doFilter(request, response);
    }
}