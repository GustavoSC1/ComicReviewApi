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

import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<UserDTO> save(@Valid @RequestBody UserNewDTO userNewDto) {
		UserDTO userDto = userService.save(userNewDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				 .buildAndExpand(userDto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(userDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> find(@PathVariable Long id) {
		UserDTO userDto = userService.find(id);
		
		return ResponseEntity.ok().body(userDto);
	}

}
