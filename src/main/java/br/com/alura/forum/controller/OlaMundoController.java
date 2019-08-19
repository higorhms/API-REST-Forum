package br.com.alura.forum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OlaMundoController {

	@GetMapping
	public String olaMundo() {
		return "Hello World !!";
	}
}
