package finalproject.onlinegardenshop.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Custom filter for intercepting HTTP requests and extracting JWT token for authentication.
 * <p>
 * This filter extends {@link GenericFilterBean} to intercept requests, extract JWT token,
 * validate the token, and set the authentication in the security context.
 * </p>
 *
 * @Slf4j                 - Lombok annotation for generating a logger field.
 * @Component             - Indicates that an annotated class is a "component".
 *                          Such classes are considered as candidates for auto-detection
 *                          when using annotation-based configuration and classpath scanning.
 * @RequiredArgsConstructor - Lombok annotation, generates a constructor for all final fields,
 *                           with parameter order same as field order.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    /**
     * The header name for authorization.
     */
    private static final String AUTHORIZATION = "Authorization";

    /**
     * The JWT provider bean for validating JWT tokens and extracting claims.
     */
    private final JwtProvider jwtProvider;

    /**
     * Intercepts a request, extracts and validates the JWT token, and sets the authentication.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @param fc       the filter chain.
     * @throws IOException      if an I/O error occurs during this filter's processing of the request.
     * @throws ServletException if the processing fails for any other reason.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtAuthentication = JwtUtils.generate(claims);
            jwtAuthentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        fc.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request header.
     *
     * @param request the HTTP servlet request.
     * @return the JWT token if present, null otherwise.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}

