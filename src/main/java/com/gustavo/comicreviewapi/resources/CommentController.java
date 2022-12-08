package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import javax.validation.Valid;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/comments")
@Api("Comment API")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@ApiOperation("Save a comment")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Comment successfully saved"),
			@ApiResponse(code = 403, message = "You are not allowed to make this request"),
			@ApiResponse(code = 404, message = "Could not find the requested data"),
			@ApiResponse(code = 422, message = "Data validation error")
	})
	@PostMapping
	public ResponseEntity<CommentDTO> save(@Valid @RequestBody CommentNewDTO commentNewDto) {
		CommentDTO commentDto = commentService.save(commentNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(commentDto.getId()).toUri();
	
		return ResponseEntity.created(uri).body(commentDto);
	}
	
	@ApiOperation("Obtains a comment details by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Comment obtained successfully"),
			@ApiResponse(code = 404, message = "Could not find the requested data")
	})
	@GetMapping("/{id}")
	public ResponseEntity<CommentDTO> find(@PathVariable Long id) {
		CommentDTO commentDto = commentService.find(id);
		
		return ResponseEntity.ok().body(commentDto);
	}
	
	@ApiOperation("Updates a comment")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully edited comment"),
			@ApiResponse(code = 403, message = "You are not allowed to make this request"),
			@ApiResponse(code = 404, message = "Could not find the requested data"),
			@ApiResponse(code = 422, message = "Data validation error")
			
	})
	@PutMapping("/{id}")
	public ResponseEntity<CommentDTO> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateDTO commentDto) {
		CommentDTO comment = commentService.update(id, commentDto);
	
		return ResponseEntity.ok().body(comment);
	}
	
	@ApiOperation("Deletes a comment by id")
	@ApiResponses(value = {
            @ApiResponse(code = 204, message = "Comment succesfully deleted"),
            @ApiResponse(code = 403, message = "You are not allowed to make this request"),
            @ApiResponse(code = 404, message = "Could not find the requested data")
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		commentService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
