package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.RateNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;
import com.gustavo.comicreviewapi.utils.UserSS;

@Service
public class RateService {
	
	private RateRepository rateRepository;
	private ComicService comicService;	
	
	public RateService(RateRepository rateRepository, ComicService comicService) {
		this.rateRepository = rateRepository;	
		this.comicService = comicService;
	}
	
	public void save(Long comicId, RateNewDTO rateDto) {		
		UserSS userAuthenticated = UserService.authenticated(); 
        
		User user = new User();
		user.setId(userAuthenticated.getId());
		
		Comic comic = comicService.findById(comicId);
		
		Rate rate = new Rate(user, comic, rateDto.getRate());
						
		rateRepository.save(rate);	
	}

}
