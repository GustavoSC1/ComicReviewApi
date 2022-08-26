package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class RateNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long userId;
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
