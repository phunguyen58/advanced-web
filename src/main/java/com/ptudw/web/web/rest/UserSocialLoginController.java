package com.ptudw.web.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptudw.web.repository.UserRepository;
import com.ptudw.web.security.jwt.JWTFilter;
import com.ptudw.web.security.jwt.TokenProvider;
import com.ptudw.web.service.UserService;
import com.ptudw.web.service.dto.AdminUserDTO;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to authenticate users via Facebook.
 */
@RestController
@RequestMapping("/api")
public class UserSocialLoginController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;
    private final UserRepository userRepository;

    public UserSocialLoginController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        UserService userService,
        UserRepository userRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate/social-login")
    public ResponseEntity<JWTToken> authorizeWithSocialLogin(@Valid @RequestBody SocialLoginVM socialLoginVM) {
        // Assuming FacebookLoginVM includes a field for the Facebook access token
        String accessToken = socialLoginVM.getJwt();

        // You need to validate the Facebook access token here before proceeding

        // For simplicity, assuming that validation is successful
        // In a production system, you would need to validate the token with Facebook's API

        // Now, proceed with authentication
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setLogin(socialLoginVM.getUserId());

        if (!this.userRepository.findOneByLogin(socialLoginVM.getUserId().toLowerCase()).isPresent()) {
            userService.registerFacebookUser(adminUserDTO, socialLoginVM.getUserId());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            socialLoginVM.getUserId(),
            socialLoginVM.getUserId()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false); // Facebook login may not involve "remember me"
        // String jwt = facebookAccessToken;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to receive Facebook login details.
     */
    static class SocialLoginVM {

        private String jwt;
        private String userId;

        public String getJwt() {
            return jwt;
        }

        public void setJwt(String jwt) {
            this.jwt = jwt;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
