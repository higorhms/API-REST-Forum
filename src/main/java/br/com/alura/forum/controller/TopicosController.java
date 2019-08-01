package br.com.alura.forum.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;

@RestController
public class TopicosController {

	@RequestMapping("/topicos")
	public List<TopicoDto> getTopicos() {
		Topico topico = new Topico("Duvida", "Dúvida com spring", new Curso("Spring Boot", "Programacao"));
		return TopicoDto.converter(Arrays.asList(topico, topico, topico));
	}
}
