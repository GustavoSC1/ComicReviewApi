package com.gustavo.comicreviewapi.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;
import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.feignClients.MarvelClient;

@Service
public class ComicService {
	
	private String publicKey;
	
	private String privateKey;
	
	private AuthorService authorService;
	
	private CharacterService characterService;
	
	private MarvelClient marvelClient;
	
	private Clock clock;
	
	public ComicService(@Value("${marvel.public_key}")String publicKey, @Value("${marvel.private_key}") String privateKey, 
			AuthorService authorService, CharacterService characterService, MarvelClient marvelClient, Clock clock) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.authorService = authorService;
		this.characterService = characterService;
		this.marvelClient = marvelClient;
		this.clock = clock;
	}
	
	public Comic fromDTO(ComicNewDTO objDto) {		
		Comic comic = new Comic();
		MarvelAPIModelDTO marvelModel = getComicByApi(objDto.getIdComicMarvel());
		
		comic.setId(Long.valueOf(marvelModel.getData().getResults().get(0).getId()));
		comic.setTitle(marvelModel.getData().getResults().get(0).getTitle());
		comic.setDescription(marvelModel.getData().getResults().get(0).getDescription());
		comic.setIsbn(marvelModel.getData().getResults().get(0).getIsbn());
		comic.setPrice(marvelModel.getData().getResults().get(0).getPrices().get(0).getPrice());
		
		for (CreatorSummaryDTO creator: marvelModel.getData().getResults().get(0).getCreators().getItems()) {
			Author obj = authorService.findByName(creator.getName());	
			
			if(obj==null) {				
				comic.getAuthors().add(new Author(null, creator.getName()));					
			} else {
				comic.getAuthors().add(obj);
			}
		}
		
		for (CharacterSummaryDTO character: marvelModel.getData().getResults().get(0).getCharacters().getItems()) {
			Character obj = characterService.findByName(character.getName());	
			
			if(obj==null) {				
				comic.getCharacters().add(new Character(null, character.getName()));					
			} else {
				comic.getCharacters().add(obj);
			}
		}
		
		return comic;		
	}
	
	public MarvelAPIModelDTO getComicByApi(Integer idComicMarvel) {
		String timeStamp = String.valueOf((int)(clock.millis() / 1000));
		String hash = getHash(timeStamp);
		
		MarvelAPIModelDTO comic = marvelClient.getComic(idComicMarvel, timeStamp, publicKey, hash);
		
		return comic;
	}
	
	public String getHash(String timeStemp) {
		String value = timeStemp+privateKey+publicKey;
		
		//Message Digest ou hash é usado para garantir a segurança de um mensagem que foi enviada por um canal/caminho inseguro. 
		//Message Digest recebe uma entrada de dados com tamanho variado e transforma em uma saída de tamanho fixo.
		MessageDigest md;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
	
		return hash.toString(16);
	}

}
