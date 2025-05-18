package com.sixmycat.catchy.feature.auth.command.application.service;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public interface OAuth2AuthenticationService {
    String handleOAuth2Login(DefaultOAuth2User oAuth2User, String registrationId);
}