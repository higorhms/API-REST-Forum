package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}") // Expression language para obter o valor do atributo expiration no
										// application.properties
	private String expiration;

	@Value("${forum.jwt.secret}") // Expression language para obter o valor do atributo expiration no
									// application.properties
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario usuario = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

		// Utilizando a biblioteca JJWT para criação do token
		return Jwts.builder().setIssuer("API do Higor").setSubject(usuario.getId().toString()).setIssuedAt(hoje)
				.setExpiration(dataExpiracao).signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isTokenValido(String token) {

		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public long recuperaIdUsuario(String token) {

		Claims corpoDoToken = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.parseLong(corpoDoToken.getSubject());
	}
}
