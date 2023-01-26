package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;

@RestController
public class LoginController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public AuthResBody authenticate(@RequestBody User authReqBody) {
		System.out.println("Auth Details: " + authReqBody);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authReqBody.getEmail(),
				authReqBody.getPassword());

		System.out.println("\nAuthentication Token Before Authentication: " + token);

		Authentication authResult = authenticationManager.authenticate(token);

		System.out.println();
		System.out.println("Authentication Token After Authentication: " + authResult);
		System.out.println();

		System.out.println(
				"Authentication Token in Security Context: " + SecurityContextHolder.getContext().getAuthentication());

		System.out.println();
		if (authResult.isAuthenticated())
			System.out.println("User is Authenticated");

		return new AuthResBody(true);
	}
}
