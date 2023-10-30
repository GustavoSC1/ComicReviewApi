package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.services.CommentService;
import com.gustavo.comicreviewapi.services.LikeService;
import com.gustavo.comicreviewapi.services.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review API")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeService likeService;
	
	@Operation(summary = "Save a review")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Review successfully saved"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping
	public ResponseEntity<ReviewDTO> save(@Valid @RequestBody ReviewNewDTO reviewNewDto) {
		ReviewDTO reviewDto = reviewService.save(reviewNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(reviewDto.getId()).toUri();
	
		return ResponseEntity.created(uri).body(reviewDto);
	}
	
	@Operation(summary = "Obtains a review details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Review obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ReviewDTO> find(@PathVariable Long id) {
		ReviewDTO reviewDto = reviewService.find(id);
		
		return ResponseEntity.ok().body(reviewDto);
	}
	
	@Operation(summary = "Updates a review")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited review"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PutMapping("/{id}")
	public ResponseEntity<ReviewDTO> update(@PathVariable Long id, @Valid @RequestBody ReviewUpdateDTO reviewDto) {
		ReviewDTO review = reviewService.update(id, reviewDto);
		
		return ResponseEntity.ok().body(review);
	}	
	
	@Operation(summary = "Deletes a review by id")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review succesfully deleted"),
            @ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
            @ApiResponse(responseCode = "404", description = "Could not find the requested data")
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		reviewService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Find comments by review")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Comments found successfully")
	})
	@GetMapping("/{reviewId}/comments")
	public ResponseEntity<Page <CommentDTO>> findCommentsByReview(
					@PathVariable Long reviewId,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="date") String orderBy,
					@RequestParam(value="direction", defaultValue="DESC") String direction) {
		
		Page<CommentDTO> list = commentService.findCommentsByReview(reviewId, page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);		
	}
	
	@Operation(summary = "Find reviews by title")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reviews found successfully")
	})
	@GetMapping
	public ResponseEntity<Page <ReviewDTO>> findByTitle(
					@RequestParam(value="title", defaultValue="") String title,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="date") String orderBy,
					@RequestParam(value="direction", defaultValue="DESC") String direction) {
		
		Page<ReviewDTO> list = reviewService.findByTitle(title, page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Save a like")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Like successfully saved"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/{reviewId}/likes")
	public ResponseEntity<Void> saveLike(@PathVariable Long reviewId, @Valid @RequestBody LikeNewDTO likeDto) {
		likeService.save(reviewId, likeDto);
		
		return ResponseEntity.ok().build();
	}
}
