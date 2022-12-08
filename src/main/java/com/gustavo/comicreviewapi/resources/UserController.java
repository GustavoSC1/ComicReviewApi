package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.dtos.UserUpdateDTO;
import com.gustavo.comicreviewapi.services.CommentService;
import com.gustavo.comicreviewapi.services.ReviewService;
import com.gustavo.comicreviewapi.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
@Api("User API")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ReviewService reviewService;
	
	@ApiOperation("Save a user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "User successfully saved"),
			@ApiResponse(code = 400, message = "This request can be processed"),
			@ApiResponse(code = 422, message = "Data validation error")
	})
	@PostMapping
	public ResponseEntity<UserDTO> save(@Valid @RequestBody UserNewDTO userNewDto) {
		UserDTO userDto = userService.save(userNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				 .buildAndExpand(userDto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(userDto);
	}
	
	@ApiOperation("Obtains a user details by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User obtained successfully"),
			@ApiResponse(code = 404, message = "Could not find the requested data")
	})
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> find(@PathVariable Long id) {
		UserDTO userDto = userService.find(id);
		
		return ResponseEntity.ok().body(userDto);
	}
	
	@ApiOperation("Updates a user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully edited user"),
			@ApiResponse(code = 403, message = "You are not allowed to make this request"),
			@ApiResponse(code = 404, message = "Could not find the requested data"),
			@ApiResponse(code = 422, message = "Data validation error")
	})
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDto) {
		UserDTO user = userService.update(id, userDto);
		
		return ResponseEntity.ok().body(user);
	}
	
	@ApiOperation("Deletes a user by id")
	@ApiResponses(value = {
            @ApiResponse(code = 204, message = "User succesfully deleted"),
            @ApiResponse(code = 403, message = "You are not allowed to make this request"),
            @ApiResponse(code = 404, message = "Could not find the requested data")
    })
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation("Find comments by user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Comments found successfully")
	})
	@GetMapping("/{userId}/comments")
	public ResponseEntity<Page <CommentDTO>> findCommentsByUser(
					@PathVariable Long userId,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="date") String orderBy,
					@RequestParam(value="direction", defaultValue="DESC") String direction) {
		
		Page<CommentDTO> list = commentService.findCommentsByUser(userId, page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);		
	}
	
	@ApiOperation("Find reviews by user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reviews found successfully")
	})
	@GetMapping("/{userId}/reviews")
	public ResponseEntity<Page <ReviewDTO>> findReviewsByUser(
					@PathVariable Long userId,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="date") String orderBy,
					@RequestParam(value="direction", defaultValue="DESC") String direction) {
		
		Page<ReviewDTO> list = reviewService.findReviewsByUser(userId, page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);	
	}

}
