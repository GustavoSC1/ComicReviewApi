package com.gustavo.comicreviewapi.builders;

import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.dtos.CommentDTO;

public class CommentDtoBuilder {
	
	private CommentDTO commentDto;
	
	private CommentDtoBuilder() {}
	
	public static CommentDtoBuilder aCommentDTO() {
		CommentDtoBuilder builder = new CommentDtoBuilder();
		builder.commentDto = new CommentDTO();
		builder.commentDto.setTitle("Ótimo review");
		builder.commentDto.setDate(LocalDateTime.of(2022, 11, 20, 22, 10));
		builder.commentDto.setContent("Parabéns pelo review, com certeza irei adquirir essa HQ!");
		
		return builder;
	}
	
	public CommentDtoBuilder withId(Long id) {
		commentDto.setId(id);
		return this;
	}
	
	public CommentDTO now() {
		return commentDto;
	}

}
