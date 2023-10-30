package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.dtos.CommentUpdateDTO;
import com.gustavo.comicreviewapi.services.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comment API")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Operation(summary = "Save a comment")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Comment successfully saved"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping
	public ResponseEntity<CommentDTO> save(@Valid @RequestBody CommentNewDTO commentNewDto) {
		CommentDTO commentDto = commentService.save(commentNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(commentDto.getId()).toUri();
	
		return ResponseEntity.created(uri).body(commentDto);
	}
	
	@Operation(summary = "Obtains a comment details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Comment obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{id}")
	public ResponseEntity<CommentDTO> find(@PathVariable Long id) {
		CommentDTO commentDto = commentService.find(id);
		
		return ResponseEntity.ok().body(commentDto);
	}
	
	@Operation(summary = "Updates a comment")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited comment"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
			
	})
	@PutMapping("/{id}")
	public ResponseEntity<CommentDTO> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateDTO commentDto) {
		CommentDTO comment = commentService.update(id, commentDto);
	
		return ResponseEntity.ok().body(comment);
	}
	
	@Operation(summary = "Deletes a comment by id")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment succesfully deleted"),
            @ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
            @ApiResponse(responseCode = "404", description = "Could not find the requested data")
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		commentService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
