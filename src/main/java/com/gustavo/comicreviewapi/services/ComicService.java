package com.gustavo.comicreviewapi.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;
import com.gustavo.comicreviewapi.feignClients.MarvelClient;

@Service
public class ComicService {
	
	private String publicKey;
	
	private String privateKey;
	
	private MarvelClient marvelClient;
	
	private Clock clock;
	
	public ComicService(@Value("${marvel.public_key}")String publicKey, 
			@Value("${marvel.private_key}") String privateKey, MarvelClient marvelClient, Clock clock) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.marvelClient = marvelClient;
		this.clock = clock;
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
