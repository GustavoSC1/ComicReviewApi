package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.RateNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.RatePK;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;

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
	
	public void save(Long comicId, RateNewDTO rateDto) {		
		User user = userService.findById(rateDto.getUserId());
		Comic comic = comicService.findById(comicId);
		
		Rate rate = findById(user, comic);
		
		if(rate == null) {
			rate = new Rate(user, comic, rateDto.getRate());
		} else {
			throw new BusinessException("User has already rated this comic!");
		}		
				
		rateRepository.save(rate);		
	}
	
	public Rate findById(User user, Comic comic) {		
		Optional<Rate> rateOptional = rateRepository.findById(new RatePK(user, comic));
		
		return rateOptional.orElse(null);
	}

}
