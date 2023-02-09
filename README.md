# Comic Book Reviews API
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/GustavoSC1/ComicReviewApi/blob/main/LICENCE)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/GustavoSC1/ComicReviewApi/maven.yml?branch=main)
[![codecov](https://codecov.io/gh/GustavoSC1/ComicReviewApi/branch/main/graph/badge.svg?token=5K6EIQ8WIG)](https://codecov.io/gh/GustavoSC1/ComicReviewApi)

API: https://comicreviewapi-production.up.railway.app

Documentação: https://comicreviewapi-production.up.railway.app/swagger-ui/

## Sobre o projeto

Comic Book Reviews é uma API REST que reúne reviews de Comics e dá uma avalização geral para cada edição com base nas pontuações individuais dos revisores. Os usuários podem postar suas próprias resenhas e/ou avaliações de Comics que serão levadas em consideração ao calcular a avaliação geral de uma edição.

Nessa API pode é possivel realizar o cadastro de usuários, o cadastro de Comics, avaliar os Comics, favoritar o Comics, fazer o review do Comics, comentar o review, dar lik em no review. Além disso, também é possivel fazer diversas buscas por usuários, comics, reviews e comentários. Para obter os dados e realizar o cadastro do Comics, o sistema consome uma API externa da MARVEL (https://developer.marvel.com/).

Obs: A integração com a API da MARVEL foi feita usando o Spring-Cloud-Feign.

A API também exibe o preço do Comic e calcula um desconto baseado no último número do ISBN e no dia da semana que o usuário está fazendo a requisição.

1 ) Dia do desconto deste Comics, baseado no último número do ISBN do comics, considerando as condicionais:

Final 0-1: segunda-feira

Final 2-3: terça-feira

Final 4-5: quarta-feira

Final 6-7: quinta-feira

Final 8-9: sexta-feira

2 ) Também existe um atributo de desconto ativo, onde a data atual do sistema é comparada com as condicionais anteriores, onde, quando for o dia ativo do 
desconto retorna true, caso contrário, false.

Exemplo A: hoje é segunda-feira, o Comics tem o ISBN XXXXXXXX1, ou seja, seu desconto será às segundas-feiras e o atributo de desconto ativo será TRUE. 
Exemplo B: hoje é quinta-feira, o Comics tem o ISBN XXXXXXXX1, ou seja, seu desconto será às segundas-feiras e o atributo de desconto ativo será FALSE.

3 ) Caso seja o dia de desconto do livro, o preço a ser exibido aparece com 10% a menos que o valor retornado pela API da MARVEL.

## Modelo conceitual
![Modelo Conceitual](https://ik.imagekit.io/gustavosc/Untitled_Diagram.drawio__1__xgWAt8PHJ.png?ik-sdk-version=javascript-1.4.3&updatedAt=1673570650222)

## Tecnologias utilizadas
### Back end
- [Java](https://www.oracle.com/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Maven](https://maven.apache.org/)
- [Swagger](https://swagger.io/)
- [H2](https://www.h2database.com/html/main.html)
- [JWT](https://jwt.io/)
- [PostgreSQL](https://www.postgresql.org/)
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://site.mockito.org/)
- [Jacoco](https://www.jacoco.org/jacoco/trunk/doc/mission.html)

## Como executar o projeto

### Back end
Pré-requisitos: Java 11

```bash
# clonar repositório
git clone https://github.com/GustavoSC1/ComicReviewApi.git

# entrar na pasta do projeto library api
cd ComicReviewApi

# executar o projeto
./mvnw spring-boot:run
```

## Autor

Gustavo da Silva Cruz

https://www.linkedin.com/in/gustavo-silva-cruz-20b128bb/
