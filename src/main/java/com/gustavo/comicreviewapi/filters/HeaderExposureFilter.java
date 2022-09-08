package com.gustavo.comicreviewapi.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class HeaderExposureFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// Sempre que um recurso é inserido, o endereço desse recurso é retornado no cabeçalho
		// Eese código abaixo vai expor o header location nas respostas.
		// Dessa forma uma aplicação front-end vai conseguir ler esse cabeçalho.
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("access-control-expose-headers", "location");
		chain.doFilter(request, response);		
	}

}
