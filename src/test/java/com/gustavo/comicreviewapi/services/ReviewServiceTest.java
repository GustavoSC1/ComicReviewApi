package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.ReviewNewDtoBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.ReviewRepository;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReviewServiceTest {
	
	ReviewService reviewService;
	
	@MockBean
	ReviewRepository reviewRepository;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ComicService comicService;
	
	@BeforeEach
	public void setUp() {
		this.reviewService = Mockito.spy(new ReviewService(reviewRepository, userService, comicService));
	}
	
	@Test
	@DisplayName("Must save a review")
	public void saveReviewTest() {
		// Scenario
		Long id = 1l;
		
		ReviewNewDTO newReview = ReviewNewDtoBuilder.aReviewNewDTO().now();
		User user = UserBuilder.aUser().withId(id).now();
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		Review savedReview = ReviewBuilder.aReview().withId(id).now();
		
		Mockito.when(userService.findById(id)).thenReturn(user);
		Mockito.when(comicService.findById(id)).thenReturn(comic);
		Mockito.doReturn(LocalDateTime.of(2022, 11, 20, 21, 50)).when(reviewService).getDateTime();
		Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(savedReview);
		
		// Execution		
		ReviewDTO savedReviewDto = reviewService.save(newReview);
	
		// Verification
		Assertions.assertThat(savedReviewDto.getId()).isEqualTo(id);
		Assertions.assertThat(savedReviewDto.getTitle()).isEqualTo("Ótima história");
		Assertions.assertThat(savedReviewDto.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 21, 50));
		Assertions.assertThat(savedReviewDto.getContent()).isEqualTo("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
	}
	
	@Test
	@DisplayName("Must get one review per id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		Review review = ReviewBuilder.aReview().withId(id).now();
		
		Mockito.when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
		
		// Execution
		Review foundReview = reviewService.findById(id);
		
		// Verification
		Assertions.assertThat(foundReview.getId()).isEqualTo(id);
		Assertions.assertThat(foundReview.getTitle()).isEqualTo("Ótima história");
		Assertions.assertThat(foundReview.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 21, 50));
		Assertions.assertThat(foundReview.getContent()).isEqualTo("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent review")
	public void reviewNotFoundByIdTest() {
		// Scenario
		Long id = 1l;
		Mockito.when(reviewRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execution and Verification
		Exception exception = assertThrows(ObjectNotFoundException.class, () -> {reviewService.findById(id);});
		
		String expectedMessage = "Object not found! Id: " + id + ", Type: " + Review.class.getName();
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
	}
	
	@Test
	@DisplayName("Must call findById method and return Review to controller")
	public void findTest() {
		// Scenario
		long id = 2l;
		
		Review review = ReviewBuilder.aReview().withId(id).now();
		
		Mockito.doReturn(review).when(reviewService).findById(id);
		
		// Execution
		ReviewDTO foundReview = reviewService.find(id);
		
		// Verification
		Assertions.assertThat(foundReview.getId()).isEqualTo(id);
		Assertions.assertThat(foundReview.getTitle()).isEqualTo("Ótima história");
		Assertions.assertThat(foundReview.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 21, 50));
		Assertions.assertThat(foundReview.getContent()).isEqualTo("A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.");
	}
	
	@Test
	@DisplayName("Must update a review")
	public void updateReviewTest() {
		// Scenario
		Long id = 2l;
		
		ReviewUpdateDTO reviewDto = new ReviewUpdateDTO("História fraca", "A HQ não mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família.");
		
		Review foundReview = ReviewBuilder.aReview().withId(id).now();
		
		Review updatedReview = new Review(id, "História fraca", LocalDateTime.of(2022, 11, 21, 19, 29), 
						"A HQ não mostra quase nada sobre o Homem-Aranha: "
						+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
						+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
						+ "família.", null, null);
		
		Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(updatedReview);
		Mockito.doReturn(LocalDateTime.of(2022, 11, 21, 19, 29)).when(reviewService).getDateTime();
		Mockito.doReturn(foundReview).when(reviewService).findById(id);
		
		// Execution
		ReviewDTO updatedReviewDto = reviewService.update(id, reviewDto);
		
		// Verification
		Assertions.assertThat(updatedReviewDto.getId()).isEqualTo(id);
		Assertions.assertThat(updatedReviewDto.getTitle()).isEqualTo("História fraca");
		Assertions.assertThat(updatedReviewDto.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 21, 19, 29));
		Assertions.assertThat(updatedReviewDto.getContent()).isEqualTo("A HQ não mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família.");
	}
	
}
