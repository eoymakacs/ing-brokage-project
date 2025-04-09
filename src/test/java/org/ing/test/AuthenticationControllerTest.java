package org.ing.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.ing.dto.AssetRequestDto;
import org.ing.dto.LoginUserDto;
import org.ing.dto.RegisterUserDto;
import org.ing.entity.Asset;
import org.ing.entity.User;
import org.ing.entity.UserRole;
import org.ing.repository.AssetRepository;
import org.ing.repository.UserRepository;
import org.ing.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Use the full Spring context
@AutoConfigureMockMvc // This will auto-configure MockMvc to test the controller layer
public class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc; // This is for sending HTTP requests to the controller

	@Autowired
	private UserRepository userRepository; // Inject the repository for validation after service call

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private JwtService jwtService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testUserSignUp() throws Exception {
//		String customer_1 = "{ \"email\": \"jsimith@gmail.com\", \"password\": \"password123\", \"fullName\": \"John Simith\" }";
//		String admin_1 = "{ \"email\": \"brokagetechsupport@ing.com\", \"password\": \"admin123\", \"fullName\": \"ING Brokage Tech Support\", \"role\": \"ADMIN\" }";

		RegisterUserDto customer_1 = new RegisterUserDto("jsimith@gmail.com", "password123", "John Simith");
		RegisterUserDto customer_2 = new RegisterUserDto("janewatson@gmail.com", "password123", "Jane Watson");
		RegisterUserDto customer_3 = new RegisterUserDto("emineoymak@gmail.com", "password123", "Emine Oymak");

		String customer_1_json = objectMapper.writeValueAsString(customer_1);
		String customer_2_json = objectMapper.writeValueAsString(customer_2);
		String customer_3_json = objectMapper.writeValueAsString(customer_3);

		RegisterUserDto admin_1 = new RegisterUserDto("brokagetechsupport@ing.com", "admin123",
				"ING Brokage Tech Support", UserRole.ADMIN);
		String admin_1_json = objectMapper.writeValueAsString(admin_1);

		// When: sending a POST request to /auth/signup to create a new user
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(customer_1_json))
				// Then: check that the response status is 200 (OK)
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.email").value(customer_1.getEmail()))
				.andExpect(jsonPath("$.role").value(UserRole.CUSTOMER.name()));

		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(customer_2_json))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.email").value(customer_2.getEmail()))
				.andExpect(jsonPath("$.role").value(UserRole.CUSTOMER.name()));

		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(customer_3_json))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.email").value(customer_3.getEmail()))
				.andExpect(jsonPath("$.role").value(UserRole.CUSTOMER.name()));

		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(admin_1_json))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.email").value(admin_1.getEmail()))
				.andExpect(jsonPath("$.role").value(UserRole.ADMIN.name()));

		// Additionally, you can check if the user was actually saved to the database
		Optional<User> optUser_1 = userRepository.findByEmail(customer_1.getEmail());
		assertNotNull(optUser_1.get());
		assertEquals(customer_1.getFullName(), optUser_1.get().getFullName());
		assertEquals(customer_1.getEmail(), optUser_1.get().getEmail());
		assertEquals(UserRole.CUSTOMER, optUser_1.get().getRole());

		Optional<User> optUser_2 = userRepository.findByEmail(customer_2.getEmail());
		assertNotNull(optUser_2.get());
		assertEquals(customer_2.getFullName(), optUser_2.get().getFullName());
		assertEquals(customer_2.getEmail(), optUser_2.get().getEmail());
		assertEquals(UserRole.CUSTOMER, optUser_2.get().getRole());

		Optional<User> optUser_3 = userRepository.findByEmail(customer_3.getEmail());
		assertNotNull(optUser_3.get());
		assertEquals(customer_3.getFullName(), optUser_3.get().getFullName());
		assertEquals(customer_3.getEmail(), optUser_3.get().getEmail());
		assertEquals(UserRole.CUSTOMER, optUser_3.get().getRole());

		Optional<User> optAdmin_1 = userRepository.findByEmail(admin_1.getEmail());
		assertNotNull(optAdmin_1.get());
		assertEquals(admin_1.getFullName(), optAdmin_1.get().getFullName());
		assertEquals(admin_1.getEmail(), optAdmin_1.get().getEmail());
		assertEquals(UserRole.ADMIN, optAdmin_1.get().getRole());

	}

	@Test
	public void testUserLogin() throws Exception {
		LoginUserDto userCredentials_1 = new LoginUserDto("jsimith@gmail.com", "password123");

		String userCredentials_1_json = objectMapper.writeValueAsString(userCredentials_1);

		// When: sending a POST request to /auth/login to login with a user
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(userCredentials_1_json))
				// Then: check that the response status is 200 (OK)
				.andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty())
				.andExpect(jsonPath("$.expiresIn").value(jwtService.getExpirationTime()));

	}

	@Test
	public void testCreateAssets() throws Exception {
		LoginUserDto userCredentials = new LoginUserDto("brokagetechsupport@ing.com", "admin123");

		String userCredentials_json = objectMapper.writeValueAsString(userCredentials);

		MvcResult loginResult = mockMvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(userCredentials_json))
				.andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty())
				.andExpect(jsonPath("$.expiresIn").value(jwtService.getExpirationTime())).andReturn();

		String token = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
		assertNotNull(token);

		MvcResult mvcAllUsers = mockMvc.perform(get("/users/").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThan(0)))).andReturn();

		List<User> users = new ObjectMapper().readValue(mvcAllUsers.getResponse().getContentAsString(),
				new TypeReference<List<User>>() {
				});

		assertNotNull(users);
		assertTrue(users.size() > 0);
		// Create TRY assets for all customers
		for (User user : users) {
			if (user.getRole() == UserRole.CUSTOMER) {
				AssetRequestDto asset_TRY = new AssetRequestDto(user.getId(), "TRY", new BigDecimal(500000),
						new BigDecimal(500000));
				String asset_TRY_Json = objectMapper.writeValueAsString(asset_TRY);
				mockMvc.perform(post("/assets/newAsset").header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(asset_TRY_Json))
						.andExpect(jsonPath("$.id").isNumber()).andReturn();
			}
		}

		for (User user : users) {
			// Check if assets have been actually saved into database
			if (user.getRole() == UserRole.CUSTOMER) {
				Optional<Asset> optAsset_1 = assetRepository.findByCustomerIdAndAssetName(user.getId(), "TRY");
				assertNotNull(optAsset_1.get());
				assertEquals(optAsset_1.get().getAssetName(), "TRY");
				assertEquals(optAsset_1.get().getUser().getId(), user.getId());
			}
		}

	}
}
