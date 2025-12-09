package com.authentication.auth.security;

import com.authentication.auth.model.User;
import com.authentication.auth.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String email = getEmail(oAuth2User);
        if (!StringUtils.hasText(email)) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            // Update user details if needed
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2User);
        }

        return new CustomOAuth2User(oAuth2User, user.getEmail());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String email = getEmail(oAuth2User);
        User user = User.builder()
                .email(email)
                .password("OAUTH2_USER") // This should be encoded in a real scenario
                .build();
        return userRepository.save(user);
    }

    private String getEmail(OAuth2User oAuth2User) {
        // Different providers might have different attribute names for email
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            // Try other common attribute names
            if (oAuth2User.getAttributes().containsKey("emails")) {
                Object emails = oAuth2User.getAttribute("emails");
                if (emails instanceof Iterable) {
                    for (Object item : (Iterable<?>) emails) {
                        if (item instanceof java.util.Map) {
                            email = (String) ((java.util.Map<?, ?>) item).get("value");
                            if (email != null) break;
                        }
                    }
                }
            }
        }
        return email;
    }
}
