package com.scm.services.impl;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResouceNotFoundException;
import com.scm.repsitories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.RegistrationResult;
import com.scm.services.UserService;
import com.scm.services.exceptions.DuplicateEmailException;
import com.scm.services.exceptions.VerificationEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Helper helper;

    @Value("${server.baseUrl:http://localhost:8080}")
    private String appBaseUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public RegistrationResult registerUser(User user) {
        String normalizedEmail = normalizeEmail(user.getEmail());
        user.setEmail(normalizedEmail);


        if (userRepo.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateEmailException("An account with this email already exists.");
        }

        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(false);
        user.setEnabled(false);
        user.setEmailToken(UUID.randomUUID().toString());

        User saveUser;
        try {
            saveUser = userRepo.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException("An account with this email already exists.", ex);
        }

        String emailLink = getEmailVerificationLink(saveUser.getEmailToken());
        try {
            emailService.sendEmail(saveUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);
            return new RegistrationResult(saveUser, true);
        } catch (MailException ex) {
            logger.error("Rolling back registration because verification email delivery failed for {}", saveUser.getEmail(), ex);
            throw new VerificationEmailException("We could not send the verification email right now. Please try again later.", ex);
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private String getEmailVerificationLink(String emailToken) {
        return appBaseUrl + "/auth/verify-email?token=" + emailToken;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 =  userRepo.findById(user.getUserId()).orElseThrow(()-> new ResouceNotFoundException("User Not Found"));

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(passwordEncoder.encode(user.getPassword()));
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(()-> new ResouceNotFoundException("User Not Found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 != null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
}
