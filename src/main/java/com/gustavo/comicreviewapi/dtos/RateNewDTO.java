package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class RateNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
		
	@NotNull(message="The rate field is required")
	@Range(min=1, max=5, message="The rate must be between 1 and 5")
	private Integer rate;
	
	public RateNewDTO() {
		
	}
	
	public RateNewDTO(Integer rate) {
		this.rate = rate;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
}
