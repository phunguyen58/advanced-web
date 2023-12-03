package com.ptudw.web.web.rest;

import com.ptudw.web.domain.FacebookValidationResult;
import com.ptudw.web.service.FacebookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class FacebookController {

    // @Autowired
    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateFacebookToken(@RequestParam String accessToken) {
        FacebookValidationResult validationResult = facebookService.validateAccessToken(accessToken);

        if (validationResult.getData().isIs_valid()) {
            // The Facebook access token is valid
            return ResponseEntity.ok("Token is valid");
        } else {
            // The Facebook access token is not valid
            return ResponseEntity.badRequest().body("Token is not valid");
        }
    }
}
