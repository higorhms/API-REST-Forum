package br.com.alura.forum.config.validacao.dto;

public class ErroDeFormularioDto {

	private String titulo;
	private String mensagem;

	public ErroDeFormularioDto(String titulo, String mensagem) {
		this.titulo = titulo;
		this.mensagem = mensagem;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

}
