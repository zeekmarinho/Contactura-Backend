package com.contactura.contactura;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwaordEncoder = new BCryptPasswordEncoder();
		
		System.out.println(passwaordEncoder.encode("root"));

	}

}
