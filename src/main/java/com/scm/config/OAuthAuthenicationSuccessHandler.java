package com.scm.config;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repsitories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class OAuthAuthenicationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String DEFAULT_PROFILE_PICTURE = "/images/default-avatar.svg";

    @Autowired
    private UserRepo userRepo;

    Logger logger = LoggerFactory.getLogger(OAuthAuthenicationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("oAuthAuthenicationSuccessHandler");

        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        String email = "";
        String picture = "";
        String name = "";
        String providerUserId = oauthUser.getName();
        Providers provider = Providers.SELF;
        String about = "";

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

            email = getAttribute(oauthUser, "email");
            picture = getAttribute(oauthUser, "picture");
            name = getAttribute(oauthUser, "name");
            provider = Providers.GOOGLE;
            about = "This account is created using google.";
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // github
            // github attributes
            String githubLogin = getAttribute(oauthUser, "login");
            email = !isBlank(getAttribute(oauthUser, "email")) ? getAttribute(oauthUser, "email")
                    : githubLogin + "@gmail.com";
            picture = getAttribute(oauthUser, "avatar_url");
            name = githubLogin;
            provider = Providers.GITHUB;
            about = "This account is created using github";
        }

        if (isBlank(email)) {
            logger.warn("OAuth login succeeded but provider {} did not return an email", authorizedClientRegistrationId);
            new DefaultRedirectStrategy().sendRedirect(request, response, "/login?error=true");
            return;
        }

        email = email.trim().toLowerCase(Locale.ROOT);
        name = isBlank(name) ? email : name;
        picture = isBlank(picture) ? DEFAULT_PROFILE_PICTURE : picture;

        User user = userRepo.findByEmail(email).orElseGet(User::new);
        if (isBlank(user.getUserId())) {
            user.setUserId(UUID.randomUUID().toString());
            user.setPassword("dummy");
        }

        if (user.getRoleList() == null || user.getRoleList().isEmpty()) {
            user.setRoleList(List.of(AppConstants.ROLE_USER));
        }

        user.setEmail(email);
        user.setProfilePic(picture);
        user.setName(name);
        user.setProviderUserId(providerUserId);
        user.setProvider(provider);
        user.setAbout(about);
        user.setEmailVerified(true);
        user.setEnabled(true);

//        DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
//
//        logger.info(user.getName());
//        user.getAttributes().forEach((key, value) -> {
//            logger.info("{} => {}", key, value);
//        });
//
//        logger.info(user.getAuthorities().toString());
//
//        User user1 = new User();
//        user1.setUserId(UUID.randomUUID().toString());
//        user1.setRoleList(List.of(AppConstants.ROLE_USER));
//        user1.setEmailVerified(true);
//        user1.setEnabled(true);
//        user1.setPassword("dummy");
//
        userRepo.save(user);
        logger.info("OAuth user saved or updated -> {}", user.getEmail());

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

    }

    private String getAttribute(DefaultOAuth2User oauthUser, String attributeName) {
        Object value = oauthUser.getAttribute(attributeName);
        return value == null ? "" : value.toString().trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
