package com.gustavo.comicreviewapi.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ReviewRepositoryTest {
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must save a review")
	public void saveReviewTest() {
		// Scenario
		Review newReview = createReview();
				
		// Execution
		Review savedReview = reviewRepository.save(newReview);
		
		// Verification
		Assertions.assertThat(savedReview.getId()).isEqualTo(1l);
	}
	
	private Review createReview() {		
		User user = new User();
		Comic comic = new Comic();
		comic.setId(1l);
		
		entityManager.persist(user);
		entityManager.persist(comic);
		
		Review review = new Review(null,"Ótima história", LocalDateTime.now(), "A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.", user, comic);
		
		return review;
	}

}
