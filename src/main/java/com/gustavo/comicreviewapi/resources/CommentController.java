package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping
	public ResponseEntity<CommentDTO> save(@Valid @RequestBody CommentNewDTO commentNewDto) {
		CommentDTO commentDto = commentService.save(commentNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(commentDto.getId()).toUri();
	
		return ResponseEntity.created(uri).body(commentDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CommentDTO> find(@PathVariable Long id) {
		CommentDTO commentDto = commentService.find(id);
		
		return ResponseEntity.ok().body(commentDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CommentDTO> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateDTO commentDto) {
		CommentDTO comment = commentService.update(id, commentDto);
	
		return ResponseEntity.ok().body(comment);
	}
	
}
