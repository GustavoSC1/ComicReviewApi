package com.gustavo.comicreviewapi.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;

@FeignClient(name = "${marvel.name}", url = "${marvel.baseUrl}") //Declara um cliente Feign
public interface MarvelClient {
	
	@GetMapping("/comics/{idComicMarvel}")
	MarvelAPIModelDTO getComic(@PathVariable("idComicMarvel") Integer idComicMarvel, @RequestParam("ts") String timeStamp,
			@RequestParam("apikey") String publicKey, @RequestParam("hash") String hashMD5);

}
