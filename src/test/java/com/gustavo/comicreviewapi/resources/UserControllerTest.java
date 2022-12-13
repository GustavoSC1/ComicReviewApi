package com.gustavo.comicreviewapi.resources;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gustavo.comicreviewapi.builders.CommentDtoBuilder;
import com.gustavo.comicreviewapi.builders.ReviewDtoBuilder;
import com.gustavo.comicreviewapi.builders.UserDtoBuilder;
import com.gustavo.comicreviewapi.builders.UserNewDtoBuilder;
import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.dtos.UserUpdateDTO;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.security.JWTUtil;
import com.gustavo.comicreviewapi.services.CommentService;
import com.gustavo.comicreviewapi.services.ReviewService;
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
	
	@MockBean
	CommentService commentService;
	
	@MockBean
	ReviewService reviewService;
	
	@MockBean
	JWTUtil jwtUtil;
	
	@MockBean
	UserDetailsService userDetailsService;
	
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
		
		UserNewDTO newUser = UserNewDtoBuilder.aUserNewDTO().now();
		UserDTO savedUser = UserDtoBuilder.aUserDTO().withId(id).now();
		
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
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(5)));			
	}
	
	@Test
	@DisplayName("Must get one user per id")
	public void findUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserDTO user = UserDtoBuilder.aUserDTO().withId(id).now();
		
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
			.andExpect(MockMvcResultMatchers.jsonPath("email").value("gu.cruz17@hotmail.com"))
			.andExpect(MockMvcResultMatchers.jsonPath("profiles[0]").value("USER"));
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
	// A anotação @WithMockUser nos ajuda a simular um usuário com um nome de usuário, uma senha 
	// e uma role no contexto de segurança do Spring Security. 
	// Usado para simular o fluxo de usuário autenticado.
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Must update a user")
	public void updateUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Fulano Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "998123899", "fulano-jesus87@hotmail.com.br");
				
		UserDTO updatedUser = new UserDTO(id, "Fulano Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "998123899", "fulano-jesus87@hotmail.com.br");
		updatedUser.addProfile(Profile.USER);
		
		BDDMockito.given(userService.update(Mockito.anyLong(), Mockito.any(UserUpdateDTO.class))).willReturn(updatedUser);
		
		String json = mapper.writeValueAsString(userUpdateDTO);
		
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
		.andExpect(MockMvcResultMatchers.jsonPath("email").value("fulano-jesus87@hotmail.com.br"))
		.andExpect(MockMvcResultMatchers.jsonPath("profiles[0]").value("USER"));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Should throw validation error when there is not enough data for user updating")
	public void updateInvalidUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		UserUpdateDTO user = new UserUpdateDTO();
		
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
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER", "ADMIN"})
	@DisplayName("Must delete a user")
	public void deleteUserTest() throws Exception {
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(USER_API.concat("/" + 1));
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@DisplayName("Must filter comments by user")
	public void findCommentsByUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		CommentDTO comment = CommentDtoBuilder.aCommentDTO().withId(id).now();
		
		List<CommentDTO> list = Arrays.asList(comment);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<CommentDTO> page = new PageImpl<CommentDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(commentService.findCommentsByUser(id, 0, 24, "date", "DESC")).willReturn(page);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_API.concat("/"+id+"/comments"))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(24))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));		
	}
	
	@Test
	@DisplayName("Must filter reviews by user")
	public void findReviewsByUserTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewDTO review = ReviewDtoBuilder.aReviewDTO().withId(id).now();
		
		List<ReviewDTO> list = Arrays.asList(review);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<ReviewDTO> page = new PageImpl<ReviewDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(reviewService.findReviewsByUser(id, 0, 24, "date", "DESC")).willReturn(page);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_API.concat("/"+id+"/reviews"))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(24))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));		
	}
	
}
