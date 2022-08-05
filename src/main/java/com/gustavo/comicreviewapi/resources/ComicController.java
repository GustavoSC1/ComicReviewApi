package com.gustavo.comicreviewapi.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

}
