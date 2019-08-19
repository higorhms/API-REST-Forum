package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalheDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.model.repository.CursoRepository;
import br.com.alura.forum.model.repository.TopicoRepository;

@RestController // Controle REST, para substituir os (ResponseBody) dos metodos
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired // Auto injeção de dependencia do spring
	private TopicoRepository topicoRepository;

	@Autowired // Auto injeção de dependencia do spring
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value = "listaDeTopicos") // Armazena o resultado em cache
	public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "id", page = 0, size = 10, direction = Direction.DESC) Pageable paginacao) {

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}
		Page<Topico> topicos = topicoRepository.pesquisaCursoPorNome(nomeCurso, paginacao);
		return TopicoDto.converter(topicos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalheDoTopicoDto> detalhar(@PathVariable Long id) {

		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(new DetalheDoTopicoDto(optional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional // Indica o hibernate que o metodo é uma transação
	@CacheEvict(cacheNames = "listaDeTopicos", allEntries = true) // Ao executar o metodo, cache será ser limpado
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriCB) {

		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriCB.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@Transactional // Indica o hibernate que o metodo é uma transação
	@PutMapping("/{id}")
	@CacheEvict(cacheNames = "listaDeTopicos", allEntries = true) // Ao executar o metodo, cache será ser limpado
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {

		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualiza(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional // Indica o hibernate que o metodo é uma transação
	@DeleteMapping("/{id}")
	@CacheEvict(cacheNames = "listaDeTopicos", allEntries = true) // Ao executar o metodo, cache será ser limpado
	public ResponseEntity<?> remover(@PathVariable Long id) {

		if (topicoRepository.findById(id).isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
