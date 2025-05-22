package com.multitenancy.back.common.filter;

import com.multitenancy.back.common.model.Const;
import com.multitenancy.back.common.utils.JwtUtils;
import com.multitenancy.back.model.user.dto.UserDto;
import com.multitenancy.back.model.user.model.LoginRequestModel;
import com.multitenancy.back.service.user.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@CrossOrigin(origins = "*")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final IUserService userService;
    private final Environment env;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env,
                                IUserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.env = env;
        this.jwtUtils = jwtUtils;
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            // 	인증정보 조회
            UserDto userDto;
            if (creds.getUserId() != null && !creds.getUserId().isEmpty()) {
                userDto = userService.confirmUser(creds.getUserId(), creds.getPassword());
            } else {
                userDto = userService.confirmUser(creds.getEmail(), creds.getPassword());
            }

            // Account Info Error
            if (userDto == null || !StringUtils.hasText(userDto.getUserId())) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("ERROR :: LOGIN FAILED");
                return null;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(),
                    creds.getPassword()
            );
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        long issueTime = now.toInstant().toEpochMilli();

        long accessExpiration = issueTime + Long.parseLong(Objects.requireNonNull(env.getProperty("token.access_expiration_time")));
        long refreshExpiration = issueTime + Long.parseLong(Objects.requireNonNull(env.getProperty("token.refresh_expiration_time")));

        String email = ((User) authentication.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserInfo(email);

        String accessToken = jwtUtils.makeAccessToken(userDto, issueTime, accessExpiration);
        String refreshToken = jwtUtils.makeRefreshToken(userDto.getUserId(), issueTime, refreshExpiration);

        response.addHeader("message", String.valueOf(Const.LOGIN_SUCCESS));
        response.setContentType("plain/text; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("SUCCESS");

        response.setHeader("AccessToken", accessToken);
        response.setHeader("RefreshToken", refreshToken);
        response.setHeader("exp", (env.getProperty("token.access_expiration_time")));
        response.setStatus(HttpStatus.OK.value());
    }
}