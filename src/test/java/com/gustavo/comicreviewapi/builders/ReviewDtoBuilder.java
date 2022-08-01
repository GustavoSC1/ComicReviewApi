package com.gustavo.comicreviewapi.builders;

import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.dtos.ReviewDTO;

public class ReviewDtoBuilder {
	
	private ReviewDTO reviewDto;
	
	public static ReviewDtoBuilder aReviewDTO() {
		ReviewDtoBuilder builder = new ReviewDtoBuilder();
		builder.reviewDto = new ReviewDTO();
		builder.reviewDto.setTitle("Ótima história");
		builder.reviewDto.setDate(LocalDateTime.of(2022, 11, 20, 21, 50));
		builder.reviewDto.setContent("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
				
		return builder;
	}
	
	public ReviewDtoBuilder withId(Long id) {
		reviewDto.setId(id);
		return this;
	}
	
	public ReviewDTO now() {
		return reviewDto;
	}

}
