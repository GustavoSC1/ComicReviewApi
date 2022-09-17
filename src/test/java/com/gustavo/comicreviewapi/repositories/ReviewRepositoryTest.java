package com.gustavo.comicreviewapi.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.CommentBuilder;
import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;

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
		Review newReview = ReviewBuilder.aReview().now();
				
		// Execution
		Review savedReview = reviewRepository.save(newReview);
		
		// Verification
		Assertions.assertThat(savedReview.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Must get one review per id")
	public void findByIdTest() {
		// Scenario
		Review review = ReviewBuilder.aReview().now();
		entityManager.persist(review);
		
		// Execution
		Optional<Review> foundReview = reviewRepository.findById(review.getId());
		
		// Verification
		Assertions.assertThat(foundReview.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Must filter reviews by comic")
	public void findReviewsByComicTest() {
		// Scenario
		Long id = 1l;
		
		Comic comic = ComicBuilder.aComic().withId(id).now();
		comic = entityManager.persist(comic);
		
		PageRequest pageRequest = PageRequest.of(0, 24, Direction.valueOf("DESC"), "date");
		
		Review review = ReviewBuilder.aReview().withComic(comic).now();
		
		entityManager.persist(review);
		
		// Execution
		Page<Review> foundReviews = reviewRepository.findReviewsByComic(id, pageRequest);
		
		// Verification
		Assertions.assertThat(foundReviews.getNumberOfElements()).isEqualTo(1);
		Assertions.assertThat(foundReviews.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(foundReviews.getTotalPages()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Must filter review")
	public void findByTitleTest() {
		// Scenario		
		PageRequest pageRequest = PageRequest.of(0, 24, Direction.valueOf("DESC"), "date");
		
		Review review = ReviewBuilder.aReview().now();
		
		entityManager.persist(review);
		
		// Execution
		Page<Review> foundReviews = reviewRepository.findByTitle("história", pageRequest);
		
		// Verification
		Assertions.assertThat(foundReviews.getNumberOfElements()).isEqualTo(1);
		Assertions.assertThat(foundReviews.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(foundReviews.getTotalPages()).isEqualTo(1);
	}	
	
	@Test
	@DisplayName("Must delete a review")
	public void deleteReviewTest() {
		// Scenario
		Review review = ReviewBuilder.aReview().now();
		review = entityManager.persist(review);
		
		Comment comment = CommentBuilder.aComment().withReview(review).now();		
		comment = entityManager.persist(comment);
		
		Review foundReview = entityManager.find(Review.class, review.getId());
		
		// Execution
		reviewRepository.delete(foundReview);
		
		Review deletedReview = entityManager.find(Review.class, review.getId());
		Comment foundComment = entityManager.find(Comment.class, comment.getId());
		
		// Verification
		Assertions.assertThat(deletedReview).isNull();
		Assertions.assertThat(foundComment).isNotNull(); // Os commentárioso do review não devem ser apagados
	}

}
