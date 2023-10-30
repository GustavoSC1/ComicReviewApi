package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavo.comicreviewapi.services.ComicService;
import com.gustavo.comicreviewapi.services.RateService;
import com.gustavo.comicreviewapi.services.ReadingService;
import com.gustavo.comicreviewapi.services.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.RateNewDTO;
import com.gustavo.comicreviewapi.dtos.ReadingNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;

@RestController
@RequestMapping("/comics")
@Tag(name = "Comic API")
public class ComicController {
	
	@Autowired
	private ComicService comicService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private RateService rateService;
	
	@Autowired
	private ReadingService readingService;
	
	@Operation(summary = "Save a comic")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Comic successfully saved"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ComicDTO> save(@Valid @RequestBody ComicNewDTO comicNewDto) {
		System.out.println("Entrou save comic");
		ComicDTO comicDto = comicService.save(comicNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(comicDto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(comicDto);
	}
	
	@Operation(summary = "Obtains a comic details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Comic obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ComicDTO> find(@PathVariable Long id) {
		ComicDTO comicDto = comicService.find(id);
		
		return ResponseEntity.ok().body(comicDto);
	}
	
	@Operation(summary = "Find comics by title")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Comics found successfully")
	})
	@GetMapping
	public ResponseEntity<Page <ComicDTO>> findByTitle(
					@RequestParam(value="title", defaultValue="") String title,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="title") String orderBy,
					@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		Page<ComicDTO> list = comicService.findByTitle(title, page, linesPerPage, orderBy, direction);
	
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Find reviews by comic")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reviews found successfully")
	})
	@GetMapping("/{comicId}/reviews")
	public ResponseEntity<Page <ReviewDTO>> findReviewsByComic(
					@PathVariable Long comicId,
					@RequestParam(value="page", defaultValue="0") Integer page,
					@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
					@RequestParam(value="orderBy", defaultValue="date") String orderBy,
					@RequestParam(value="direction", defaultValue="DESC") String direction) {
		
		Page<ReviewDTO> list = reviewService.findReviewsByComic(comicId, page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);		
	}
	
	@Operation(summary = "Save a rate")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rate successfully saved"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/{comicId}/ratings")
	public ResponseEntity<Void> saveRate(@PathVariable Long comicId, @Valid @RequestBody RateNewDTO rateDto) {
		rateService.save(comicId, rateDto);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Save a reading")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reading successfully saved"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/{comicId}/readings")
	public ResponseEntity<Void> saveReading(@PathVariable Long comicId, @Valid @RequestBody ReadingNewDTO readingDto) {
		readingService.save(comicId, readingDto);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Deletes a comic by id")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comic succesfully deleted"),
            @ApiResponse(responseCode = "404", description = "Could not find the requested data"),
            @ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
    })
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		comicService.delete(id);
		
		return ResponseEntity.noContent().build();
	}

}
