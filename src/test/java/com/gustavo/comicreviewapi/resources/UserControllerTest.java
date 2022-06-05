package com.gustavo.comicreviewapi.resources;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.services.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
	
	static String USER_API = "/users";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserService userService;
	
	ObjectMapper mapper;
	
	@BeforeEach
	public void setUp() {
		// Adicionando módulo para o jackson suportar o LocalDate
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Test
	@DisplayName("Must save a user")
	public void saveUserTest() throws Exception {
		// Scenario		
		long id = 2l;
		
		UserNewDTO newUser = new UserNewDTO("Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		UserDTO savedUser = new UserDTO(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		savedUser.setId(id);
		
		BDDMockito.given(userService.save(Mockito.any(UserNewDTO.class))).willReturn(savedUser);
		
		String json = mapper.writeValueAsString(newUser);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(USER_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);		
		// Verification
		mvc
		.perform(request)
		.andExpect( MockMvcResultMatchers.status().isCreated() )
		.andExpect( MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.containsString("/users/"+id)));
	}
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for user creation")
	public void createInvalidUserTest() throws Exception {
		// Scenario
		UserNewDTO newUser = new UserNewDTO();
		newUser.setBirthDate(LocalDate.of(2022, 10, 17));
		
		String json = mapper.writeValueAsString(newUser);
				
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(USER_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));			
	}
}
