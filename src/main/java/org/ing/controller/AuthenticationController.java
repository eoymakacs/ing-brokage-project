package org.ing.controller;

import org.ing.dto.LoginResponse;
import org.ing.dto.LoginUserDto;
import org.ing.dto.RegisterUserDto;
import org.ing.dto.RegisterUserResponseDto;
import org.ing.entity.User;
import org.ing.security.JwtService;
import org.ing.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;

	private final AuthenticationService authenticationService;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);
		RegisterUserResponseDto registerUserResponse = new RegisterUserResponseDto(registeredUser.getId(),
				registeredUser.getFullName(), registeredUser.getEmail(), registeredUser.getPassword(),
				registeredUser.getRole(), registeredUser.isCredentialsNonExpired(),
				registeredUser.isAccountNonExpired(), registeredUser.isAccountNonLocked(), registeredUser.getUsername(),
				registeredUser.isEnabled());

		return ResponseEntity.ok(registerUserResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}