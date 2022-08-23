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
	
	public RateService(RateRepository rateRepository) {
		this.rateRepository = rateRepository;	
	}
	
	public Rate findById(User user, Comic comic) {
		Optional<Rate> rateOptional = rateRepository.findById(new RatePK(user, comic));
		
		return rateOptional.orElse(null);
	}

}
