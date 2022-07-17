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

import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.services.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping
	public ResponseEntity<ReviewDTO> save(@Valid @RequestBody ReviewNewDTO reviewNewDto) {
		ReviewDTO reviewDto = reviewService.save(reviewNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(reviewDto.getId()).toUri();
	
		return ResponseEntity.created(uri).body(reviewDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReviewDTO> find(@PathVariable Long id) {
		ReviewDTO reviewDto = reviewService.find(id);
		
		return ResponseEntity.ok().body(reviewDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ReviewDTO> update(@PathVariable Long id, @Valid @RequestBody ReviewUpdateDTO reviewDto) {
		ReviewDTO review = reviewService.update(id, reviewDto);
		
		return ResponseEntity.ok().body(review);
	}

}
