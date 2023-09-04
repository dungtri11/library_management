package com.example.demo;

import com.example.demo.common.Authority;
import com.example.demo.entity.User;
import com.example.demo.jwt.Header;
import com.example.demo.jwt.Jwt;
import com.example.demo.jwt.Payload;
import com.example.demo.utils.Base64Handler;
import com.example.demo.utils.ClassHandler;
import com.example.demo.utils.ObjectHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

	@Test
	public void customTesting() {
		Payload payload = new Payload("johndoe", Authority.LIBRARIAN);
		Jwt jwt = new Jwt(new Header(), payload, "MySecret");

		System.out.println(jwt.getSpecification());
	}

}
