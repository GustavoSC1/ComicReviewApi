package com.gustavo.comicreviewapi.feignClients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.test.context.ActiveProfiles;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;

import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;

import org.assertj.core.api.Assertions;

@ActiveProfiles("test")
public class MarvelClientTest {

	static String BASE_URL = "https://gateway.marvel.com:443/v1/public";
	
	static String RESPONSE_COMIC = "{\n" + 
			"	\"data\": {\n" + 
			"		\"results\": [\n" + 
			"			{\n" + 
			"				\"id\": 10785, \n" +		
			"				\"title\": \"Homem-Aranha: Eternamente jovem\", \n" +
			"				\"description\": \"Na esperança de obter algumas fotos de seu alter ego aracnídeo em ação, Peter Parker sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal da cidade.\", \n" +
			"				\"isbn\": \"9786555612752\", \n" +
			"				\"prices\": [{\n" +
			"					\"price\": 38.61 \n" +
			"				}], \n" +
			"				\"creators\": {\n" +
			"					\"items\": [\n" +
			"						{\n" +
			"							\"name\": \"Stefan Petrucha\" \n" +
			"						}\n" +
			"					]\n" +
			"				},\n" +
			"				\"characters\": {\n" +
			"					\"items\": [\n" +
			"						{\n" +
			"							\"name\": \"Homem Aranha\" \n" +
			"						}\n" +
			"					]\n" +
			"				}\n" +
			"			}\n" + 
			"		]\n" + 
			"	}\n" + 
			"}";
	
	MarvelClient marvelClient;
	
	@Test
	@DisplayName("Must get one comic per id")
    public void getComicTest() {
		// Scenario
        this.buildFeignClient(new MockClient().ok(
                HttpMethod.GET,
                BASE_URL.concat("/comics/1?ts=1655238166&apikey=ae78641e8976ffdf3fd4b71254a3b9bf&hash=c6fc42667498ea8081a22f4570b42d03"),
                RESPONSE_COMIC));
        
        // Execution
        MarvelAPIModelDTO comic = marvelClient.getComic(1, "1655238166", "ae78641e8976ffdf3fd4b71254a3b9bf", "c6fc42667498ea8081a22f4570b42d03");       
        
        // Verification
        Assertions.assertThat(comic.getData().getResults().get(0).getId()).isEqualTo(10785);
        Assertions.assertThat(comic.getData().getResults().get(0).getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
        Assertions.assertThat(comic.getData().getResults().get(0).getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter ego aracnídeo em ação, Peter Parker sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal da cidade.");
        Assertions.assertThat(comic.getData().getResults().get(0).getIsbn()).isEqualTo("9786555612752");
        Assertions.assertThat(comic.getData().getResults().get(0).getPrices().get(0).getPrice()).isEqualTo(38.61f);
        Assertions.assertThat(comic.getData().getResults().get(0).getCharacters().getItems().get(0).getName()).isEqualTo("Homem Aranha");
        Assertions.assertThat(comic.getData().getResults().get(0).getCreators().getItems().get(0).getName()).isEqualTo("Stefan Petrucha");
    }
		
	private void buildFeignClient(MockClient mockClient) {
        marvelClient = Feign.builder()
                .client(mockClient)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target(MarvelClient.class, BASE_URL);
    }

}
