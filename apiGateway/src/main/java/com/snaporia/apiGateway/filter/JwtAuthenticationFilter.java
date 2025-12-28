package com.snaporia.apiGateway.filter;

import com.snaporia.apiGateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String path=request.getRequestURI();
        System.out.println("request uri is "+path);

         // public endpoints
        if (path.startsWith("/api/v1/auth/public")){
            System.out.println("public endpoint");
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("reached");
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Missing Token");
            return;
        }
        String token=header.substring(7);
        try{
            Claims claims=jwtUtil.parseToken(token);
            String username = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get("authorities");
            List<SimpleGrantedAuthority> grantedAuthorities =
                    authorities.stream().map(SimpleGrantedAuthority::new).toList();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
