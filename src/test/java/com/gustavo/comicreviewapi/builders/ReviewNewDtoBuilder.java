package com.gustavo.comicreviewapi.builders;

import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;

public class ReviewNewDtoBuilder {
	
	private ReviewNewDTO reviewNewDto;
	
	private ReviewNewDtoBuilder() {}
	
	public static ReviewNewDtoBuilder aReviewNewDTO() {
		ReviewNewDtoBuilder builder = new ReviewNewDtoBuilder();
		builder.reviewNewDto = new ReviewNewDTO();
		builder.reviewNewDto.setTitle("Ótima história");
		builder.reviewNewDto.setContent("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
		builder.reviewNewDto.setUserId(1l);
		builder.reviewNewDto.setComicId(1l);
		
		return builder;
	}
	
	public ReviewNewDTO now() {
		return reviewNewDto;
	}

}
