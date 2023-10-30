package com.gustavo.comicreviewapi.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
        description = "API that gathers comic book reviews and gives an overall rating for each issue based on individual reviewer scores. Users can post their own Comics reviews and/or ratings which will be taken into account when calculating an issue's overall rating.",
        version = "V2.0.0",
        title = "Comic Book Reviews API",
        contact = @Contact(
            name = "Gustavo da Silva Cruz",
            email = "gu.cruz17@hotmail.com",
            url = "https://github.com/GustavoSC1"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0"
        )
    )
)
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfiguration {
	
}
