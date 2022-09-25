package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.ReadingNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Reading;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.ReadingRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;

@Service
public class ReadingService {
	
	private ReadingRepository readingRepository;
	private ComicService comicService;
	
	public ReadingService(ReadingRepository readingRepository, ComicService comicService) {
		this.readingRepository = readingRepository;
		this.comicService = comicService;
	}
	
	public void save(Long comicId, ReadingNewDTO readingDto) {
		
		UserSS userAuthenticated = UserService.authenticated();
		
		if(userAuthenticated==null) {
			throw new AuthorizationException("Access denied");
		}
		
		User user = new User();
		user.setId(userAuthenticated.getId());
		
		Comic comic = comicService.findById(comicId);
		
		Reading reading = new Reading(user, comic, readingDto.getReading(), readingDto.getFavourit());
		
		readingRepository.save(reading);		
	}

}
