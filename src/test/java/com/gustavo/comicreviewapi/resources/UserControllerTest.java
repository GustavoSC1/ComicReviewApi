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
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.services.UserService;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

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
		
		UserNewDTO newUser = new UserNewDTO("Gustavo Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		UserDTO savedUser = createUserDTO();
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
	public void saveInvalidUserTest() throws Exception {
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
	
	@Test
	@DisplayName("Must get one user per id")
	public void findUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserDTO user = createUserDTO();
		user.setId(id);
		
		BDDMockito.given(userService.find(id)).willReturn(user);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(USER_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
			.andExpect(MockMvcResultMatchers.jsonPath("name").value("Gustavo Silva Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("phone").value("998123899"))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value("gu.cruz17@hotmail.com"));
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent user")
	public void userNotFoundByIdTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		BDDMockito.given(userService.find(id)).willThrow(new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + User.class.getName()));
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(USER_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("error").value("Not found"))
		.andExpect(MockMvcResultMatchers.jsonPath("message").value("Object not found! Id: " + id + ", Type: " + User.class.getName()));		
	}
	
	@Test
	@DisplayName("Must update a user")
	public void updateUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserDTO userDto = new UserDTO(id, "Fulano Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "998123899", "fulano-jesus87@hotmail.com.br");
		
		BDDMockito.given(userService.update(Mockito.anyLong(), Mockito.any(UserDTO.class))).willReturn(userDto);
		
		String json = mapper.writeValueAsString(userDto);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.put(USER_API.concat("/"+id))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		// Verification
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
		.andExpect(MockMvcResultMatchers.jsonPath("name").value("Fulano Cauê Calebe Jesus"))
		.andExpect(MockMvcResultMatchers.jsonPath("phone").value("998123899"))
		.andExpect(MockMvcResultMatchers.jsonPath("email").value("fulano-jesus87@hotmail.com.br"));
	}
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for user updating")
	public void updateInvalidUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserDTO user = new UserDTO();
		user.setBirthDate(LocalDate.of(2022, 10, 17));
		
		String json = mapper.writeValueAsString(user);
				
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.put(USER_API.concat("/"+id))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(5)));
	}
	
	public UserDTO createUserDTO() {
		return new UserDTO(null,"Gustavo Silva Cruz", LocalDate.of(1996, 10, 17), "998123899", "gu.cruz17@hotmail.com");
	}
}
