package com.gustavo.comicreviewapi.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ComicService {
	
	public String publicKey;
	
	public String privateKey;
	
	public ComicService(@Value("${marvel.public_key}")String publicKey, 
			@Value("${marvel.private_key}") String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
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
