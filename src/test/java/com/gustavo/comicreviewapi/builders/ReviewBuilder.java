package com.gustavo.comicreviewapi.builders;

import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Review;

public class ReviewBuilder {
	
	private Review review;
	
	private ReviewBuilder() {}
	
	public static ReviewBuilder aReview() {
		ReviewBuilder builder = new ReviewBuilder();
		builder.review = new Review();
		builder.review.setTitle("Ótima história");
		builder.review.setDate(LocalDateTime.of(2022, 11, 20, 21, 50));
		builder.review.setContent("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
		
		return builder;
	}
	
	public ReviewBuilder withComic(Comic comic) {
		review.setComic(comic);
		return this;
	}
	
	public ReviewBuilder withId(Long id) {
		review.setId(id);
		return this;
	}
	
	public Review now() {
		return review;
	}
	
}
