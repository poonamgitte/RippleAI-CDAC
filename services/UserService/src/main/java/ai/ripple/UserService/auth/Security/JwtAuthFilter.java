package ai.ripple.UserService.auth.Security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired 
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        FilterChain filterChain)
	        throws ServletException, IOException {

	    try {
	        String token = null;

	        if (request.getCookies() != null) {
	            for (Cookie cookie : request.getCookies()) {
	                if ("jwt".equals(cookie.getName())) {
	                    token = cookie.getValue();
	                    break;
	                }
	            }
	        }

	        if (token != null &&
	            SecurityContextHolder.getContext().getAuthentication() == null) {

	            Claims claims = jwtUtil.validateToken(token);

	            UsernamePasswordAuthenticationToken auth =
	                    new UsernamePasswordAuthenticationToken(
	                            claims.getSubject(),
	                            null,
	                            List.of(new SimpleGrantedAuthority(
	                                    claims.get("role").toString()))
	                    );

	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }

	    } catch (Exception e) {
	        SecurityContextHolder.clearContext();
	    }

	    filterChain.doFilter(request, response);
	}

}
