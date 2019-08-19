package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // Ativa o spring security no projeto
@Configuration // Informa ao spring para carregar estas configurações de segurança
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticationService autenticationService;

	// Autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// Esse metodo usa o BCRYPT para criptografar a senha ao buscar no banco
		auth.userDetailsService(autenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Esse metodo informa ao spring os endereços que devem ser autorizados sem
		// autenticação
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll()
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll().anyRequest().authenticated().and().formLogin();
	}

	// Recursos estáticos
	@Override
	public void configure(WebSecurity web) throws Exception {
	}

}
