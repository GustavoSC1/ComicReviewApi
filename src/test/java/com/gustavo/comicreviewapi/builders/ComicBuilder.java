package com.gustavo.comicreviewapi.builders;

import com.gustavo.comicreviewapi.entities.Comic;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Character;

public class ComicBuilder {
	
	private Comic comic;
	
	private ComicBuilder() {}
	
	public static ComicBuilder aComic() {
		ComicBuilder builder = new ComicBuilder();
		builder.comic = new Comic();
		builder.comic.setTitle("Homem-Aranha: Eternamente jovem");
		builder.comic.setDescription("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		builder.comic.setIsbn("9786555612752");
		builder.comic.setPrice(38.61F);
		
		return builder;
	}
	
	public ComicBuilder withCharactersList(Character... params) {
		comic.setCharacters(Stream.of(params).collect(Collectors.toSet()));
		return this;
	}
	
	public ComicBuilder withAuthorsList(Author... params) {
		comic.setAuthors(Stream.of(params).collect(Collectors.toSet()));
		return this;
	}
	
	public ComicBuilder withId(Long id) {
		comic.setId(id);
		return this;
	}
	
	public Comic now() {
		return comic;
	}

}
