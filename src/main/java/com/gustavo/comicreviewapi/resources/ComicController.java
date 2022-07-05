package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavo.comicreviewapi.services.ComicService;
import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;

@RestController
@RequestMapping("/comics")
public class ComicController {
	
	@Autowired
	private ComicService comicService;
	
	@PostMapping
	public ResponseEntity<ComicDTO> save(@Valid @RequestBody ComicNewDTO comicNewDto) {
		ComicDTO comicDto = comicService.save(comicNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(comicDto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(comicDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ComicDTO> find(@PathVariable Long id) {
		ComicDTO comicDto = comicService.find(id);
		
		return ResponseEntity.ok().body(comicDto);
	}

}
