package com.gustavo.comicreviewapi.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ComicServiceTest {
	
	ComicService comicService;
	
	@BeforeEach
	public void setUp() {
		this.comicService = new ComicService("ae78641e8976ffdf3fd4b71254a3b9bf", "eb9fd0d8r8745cd0d554fb2c0e7896dab3bb745");		
	}
	
	@Test
	@DisplayName("Must create a hash code in MD5")
	public void getHashTest() {		
		String timeStemp = "1655238166";
		
		String hash = comicService.getHash(timeStemp);
		
		Assertions.assertThat(hash).isEqualTo("c6fc42667498ea8081a22f4570b42d04");
	}

}
