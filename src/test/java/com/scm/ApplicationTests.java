package com.scm;

import com.scm.services.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	void contextLoads() {
	}

//	@Autowired
//	private EmailService service;
//
//	@Test
//	@Disabled("Requires a real SMTP setup and should not run in the default test suite.")
//	void sendEmailTest() {
//		service.sendEmail("yourEmailId", "Just managing the emails",
//				"this is scm project working on email service");
//	}
}
