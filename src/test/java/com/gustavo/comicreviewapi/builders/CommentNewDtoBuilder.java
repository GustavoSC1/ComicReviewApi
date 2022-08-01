package com.gustavo.comicreviewapi.builders;

import com.gustavo.comicreviewapi.dtos.CommentNewDTO;

public class CommentNewDtoBuilder {
	
	private CommentNewDTO commentNewDto;
	
	public static CommentNewDtoBuilder aCommentNewDTO() {
		CommentNewDtoBuilder builder = new CommentNewDtoBuilder();
		builder.commentNewDto = new CommentNewDTO();
		builder.commentNewDto.setTitle("Ótimo review");
		builder.commentNewDto.setContent("Parabéns pelo review, com certeza irei adquirir essa HQ!");
		builder.commentNewDto.setUserId(1l);
		builder.commentNewDto.setReviewId(1l);
		
		return builder;
	}
	
	public CommentNewDTO now() {
		return commentNewDto;
	}

}
