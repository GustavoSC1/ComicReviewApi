package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.RatePK;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;

@Service
public class RateService {
	
	private RateRepository rateRepository;
	private UserService userService;
	private ComicService comicService;	
	
	public RateService(RateRepository rateRepository, UserService userService, ComicService comicService) {
		this.rateRepository = rateRepository;		
		this.userService = userService;
		this.comicService = comicService;
	}
	
	public Rate findById(Long userId, Long comicId) {
		User user = userService.findById(userId);
		Comic comic = comicService.findById(comicId);
		Optional<Rate> rateOptional = rateRepository.findById(new RatePK(user, comic));
		
		return rateOptional.orElse(null);
	}

}
