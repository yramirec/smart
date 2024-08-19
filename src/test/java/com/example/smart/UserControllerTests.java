package com.example.smart;

import com.example.smart.controller.UserController;
import com.example.smart.dto.PhoneDTO;
import com.example.smart.dto.UserDTO;
import com.example.smart.entity.Users;
import com.example.smart.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTests {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void testRegisterUserSuccess() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Juan Rodriguez");
		userDTO.setEmail("juan@rodriguez.org");
		userDTO.setPassword("Hunter2@password");
		userDTO.setPhones(Collections.singletonList(new PhoneDTO("1234567", "1", "57")));

		Users user = new Users();
		user.setId(UUID.randomUUID());
		user.setName("Juan Rodriguez");
		user.setEmail("juan@rodriguez.org");
		user.setPassword("Hunter2@password");

		when(userService.registerUser(any(UserDTO.class))).thenReturn(user);

		mockMvc.perform(post("/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("Juan Rodriguez"))
				.andExpect(jsonPath("$.email").value("juan@rodriguez.org"));
	}

	@Test
	void testRegisterUserEmailAlreadyExists() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Juan Rodriguez");
		userDTO.setEmail("juan@rodriguez.org");
		userDTO.setPassword("Hunter2@password");
		userDTO.setPhones(Collections.singletonList(new PhoneDTO("1234567", "1", "57")));

		when(userService.registerUser(any(UserDTO.class)))
				.thenThrow(new IllegalArgumentException("El correo ya está registrado"));

		mockMvc.perform(post("/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
	}
}