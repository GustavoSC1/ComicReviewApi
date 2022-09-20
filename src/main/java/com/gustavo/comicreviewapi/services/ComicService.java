package com.gustavo.comicreviewapi.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;
import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.feignClients.MarvelClient;
import com.gustavo.comicreviewapi.repositories.ComicRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@Service
public class ComicService {
	
	private String publicKey;
	
	private String privateKey;
	
	private ComicRepository comicRepository;
	
	private AuthorService authorService;
	
	private CharacterService characterService;
	
	private MarvelClient marvelClient;
	
	private Clock clock;
	
	public ComicService(@Value("${marvel.public_key}")String publicKey, @Value("${marvel.private_key}") String privateKey, 
			ComicRepository comicRepository, AuthorService authorService, CharacterService characterService, 
			MarvelClient marvelClient, Clock clock) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.comicRepository = comicRepository;
		this.authorService = authorService;
		this.characterService = characterService;
		this.marvelClient = marvelClient;
		this.clock = clock;
	}	
	
	public ComicDTO save(ComicNewDTO objDto) {
		Comic comic = fromDTO(objDto);
		comic = comicRepository.save(comic);
		ComicDTO comicDto= new ComicDTO(comic);
		checkDiscount(comicDto);
		
		return comicDto;
	}
		
	public Comic fromDTO(ComicNewDTO objDto) {		
		if(comicRepository.existsById(Long.valueOf(objDto.getIdComicMarvel()))) {
			throw new BusinessException("Comic already registered!");
		} else {
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
	
	@Transactional(readOnly = true)
	public ComicDTO find(Long id) {
		Comic comic = findById(id);
		ComicDTO comicDto = new ComicDTO(comic);
		checkDiscount(comicDto);
		return comicDto;
	}
		
	public Comic findById(Long id) {
		Optional<Comic> comicOptional = comicRepository.findById(id);
		Comic comic = comicOptional.orElseThrow(() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Comic.class.getName()));
		
		return comic;
	}
	
	@Transactional(readOnly = true)
	public Page<ComicDTO> findByTitle(String title, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<ComicDTO> comicDTO = comicRepository.findByTitle(title, pageRequest).map(obj -> new ComicDTO(obj));
		
		for(ComicDTO objDto: comicDTO) {
			checkDiscount(objDto);
		}			
		
		return comicDTO;
	}
	
	public void delete(Long id) {
		Comic foundComic = findById(id);
		
		comicRepository.delete(foundComic);
	}
	
	public void checkDiscount(ComicDTO comicDto) {
		Double percentual = 10.0 / 100.0; 
		
		Date data = getDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int day = cal.get(Calendar.DAY_OF_WEEK);
				
		comicDto.setActiveDiscount(false);
		
		char lastNumber = comicDto.getIsbn().charAt(comicDto.getIsbn().length() - 1);
		
		if ((lastNumber == '0' || lastNumber == '1') && day == 2)  {
			comicDto.setActiveDiscount(true);
		} else if ((lastNumber == '2' || lastNumber == '3') &&  day == 3) {
			comicDto.setActiveDiscount(true);
		} else if (( lastNumber == '4' || lastNumber == '5') && day == 4) {
			comicDto.setActiveDiscount(true);
		} else if ((lastNumber == '6' || lastNumber == '7') && day == 5) {
			comicDto.setActiveDiscount(true);
		} else if ((lastNumber == '8' || lastNumber == '9') && day == 6) {
			comicDto.setActiveDiscount(true);
		}
		
		if(comicDto.getActiveDiscount()) {
			Double newValue = comicDto.getPrice() - (percentual * comicDto.getPrice());
			BigDecimal bd = new BigDecimal(newValue).setScale(2, RoundingMode.HALF_EVEN);
			comicDto.setPrice(bd.floatValue());
		}		
		
	}
	
	public Date getDate() {
		return new Date();
	}

}
