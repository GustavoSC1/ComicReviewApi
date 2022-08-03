package com.gustavo.comicreviewapi.builders;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDataContainerDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;

public class FeignMarvelAPIModelDtoBuilder {
	
	private MarvelAPIModelDTO marvelAPIModelDto;
	
	private FeignMarvelAPIModelDtoBuilder() {}
	
	public static FeignMarvelAPIModelDtoBuilder aMarvelAPIModelDTO() {
		FeignMarvelAPIModelDtoBuilder builder = new FeignMarvelAPIModelDtoBuilder();
		builder.marvelAPIModelDto = new MarvelAPIModelDTO();
		
		return builder;
	}
	
	public FeignMarvelAPIModelDtoBuilder withComicDtoList(ComicDTO... params) {
		ComicDataContainerDTO comicDataContainerDTO = new ComicDataContainerDTO();
		comicDataContainerDTO.setResults(Stream.of(params).collect(Collectors.toList()));
		marvelAPIModelDto.setData(comicDataContainerDTO);
		
		return this;
	}
	
	public MarvelAPIModelDTO now() {
		return marvelAPIModelDto;
	}
	
}
