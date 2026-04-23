package com.scm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("prod")
public class MailConfigStartupValidator implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MailConfigStartupValidator.class);

    private final String mailPassword;
    private final String mailFrom;
    private final boolean failFast;

    public MailConfigStartupValidator(
            @Value("${spring.mail.password:}") String mailPassword,
            @Value("${app.mail.from:}") String mailFrom,
            @Value("${app.mail.fail-fast:false}") boolean failFast
    ) {
        this.mailPassword = mailPassword;
        this.mailFrom = mailFrom;
        this.failFast = failFast;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> missing = new ArrayList<>();

        if (mailPassword == null || mailPassword.isBlank()) {
            missing.add("MAIL_PASSWORD / spring.mail.password");
        }

        boolean invalidSenderAddress = false;

        if (mailFrom == null || mailFrom.isBlank()) {
            missing.add("MAIL_FROM / app.mail.from");
        } else if (!mailFrom.contains("@")) {
            invalidSenderAddress = true;
        }

        if (missing.isEmpty() && !invalidSenderAddress) {
            return;
        }

        StringBuilder message = new StringBuilder();
        if (!missing.isEmpty()) {
            message.append("Missing required mail configuration: ")
                    .append(String.join(", ", missing))
                    .append(". Set these environment variables before sending emails.");
        }

        if (invalidSenderAddress) {
            if (message.length() > 0) {
                message.append(' ');
            }
            message.append("Invalid app.mail.from value. It must be a valid sender email address.");
        }

        if (failFast) {
            throw new IllegalStateException(message.toString());
        }

        logger.warn("{} Startup will continue, but email delivery is disabled until the mail settings are configured.", message);
    }
}
