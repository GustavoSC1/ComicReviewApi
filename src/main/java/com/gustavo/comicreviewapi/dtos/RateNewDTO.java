package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class RateNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The user id field is required")
	@Min(value=1, message="User id cannot be less than 1")
	private Long userId;
	
	@NotNull(message="The rate field is required")
	@Range(min=1, max=5, message="The rate must be between 1 and 5")
	private Integer rate;
	
	public RateNewDTO() {
		
	}
	
	public RateNewDTO(Long userId, Integer rate) {
		this.userId = userId;
		this.rate = rate;
	}

	public Long getUserId() {
		return userId;
	}

	public Integer getRate() {
		return rate;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
}
