package com.gustavo.comicreviewapi.builders;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicPriceDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorSummaryDTO;

public class FeignComicDtoBuilder {
	
	private ComicDTO comicDto;
	
	private FeignComicDtoBuilder() {}
	
	public static FeignComicDtoBuilder aFeignComicDTO() {
		FeignComicDtoBuilder builder = new FeignComicDtoBuilder();
		builder.comicDto = new ComicDTO();
		builder.comicDto.setTitle("Homem-Aranha: Eternamente jovem");
		builder.comicDto.setIsbn("9786555612752");
		builder.comicDto.setDescription("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		
		return builder;
	}
	
	public FeignComicDtoBuilder withCharactersList(CharacterSummaryDTO... params) {
		CharacterListDTO characterListDTO = new CharacterListDTO();
		characterListDTO.setItems(Stream.of(params).collect(Collectors.toList()));
		comicDto.setCharacters(characterListDTO);
		return this;
	}
	
	public FeignComicDtoBuilder withCreatorsList(CreatorSummaryDTO... params) {
		CreatorListDTO creatorListDTO = new CreatorListDTO();
		creatorListDTO.setItems(Stream.of(params).collect(Collectors.toList()));
		comicDto.setCreators(creatorListDTO);
		return this;
	}
	
	public FeignComicDtoBuilder withPrice(ComicPriceDTO... params) {
		comicDto.setPrices(Stream.of(params).collect(Collectors.toList()));
		return this;
	}
	
	public FeignComicDtoBuilder withId(Integer id) {
		comicDto.setId(id);
		return this;
	}
	
	public ComicDTO now() {
		return comicDto;
	}

}
