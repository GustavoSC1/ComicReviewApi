package com.gustavo.comicreviewapi.builders;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gustavo.comicreviewapi.dtos.AuthorDTO;
import com.gustavo.comicreviewapi.dtos.CharacterDTO;
import com.gustavo.comicreviewapi.dtos.ComicDTO;

public class ComicDtoBuilder {
	
	private ComicDTO comicDto;
	
	private ComicDtoBuilder() {}
	
	public static ComicDtoBuilder aComicDTO() {
		ComicDtoBuilder builder = new ComicDtoBuilder();
		builder.comicDto = new ComicDTO();
		builder.comicDto.setTitle("Homem-Aranha: Eternamente jovem");
		builder.comicDto.setPrice(38.61F);
		builder.comicDto.setIsbn("9786555612752");		
		builder.comicDto.setDescription("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		builder.comicDto.setAverageRating(0.0);
		builder.comicDto.setActiveDiscount(false);
						
		return builder;
	}
	
	public ComicDtoBuilder withCharactersDtoList(CharacterDTO... params) {
		comicDto.setCharacters(Stream.of(params).collect(Collectors.toSet()));
		return this;
	}
	
	public ComicDtoBuilder withAuthorsList(AuthorDTO... params) {
		comicDto.setAuthors(Stream.of(params).collect(Collectors.toSet()));
		return this;
	}
	
	public ComicDtoBuilder withId(Long id) {
		comicDto.setId(id);
		return this;
	}
	
	public ComicDTO now() {
		return comicDto;
	}

}
