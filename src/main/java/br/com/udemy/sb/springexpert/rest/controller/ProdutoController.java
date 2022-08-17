package br.com.udemy.sb.springexpert.rest.controller;

import br.com.udemy.sb.springexpert.domain.entity.Produto;
import br.com.udemy.sb.springexpert.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/{id}")
    public Produto findById(@PathVariable Integer id)  {
        return produtoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtoRepository.findById(id).map(produtoEncontrado -> {
            produtoRepository.delete(produtoEncontrado);
            return produtoEncontrado;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Produto produto, @PathVariable Integer id) {
        produtoRepository.findById(id).map(produtoEncontrado -> {
            produto.setId(produtoEncontrado.getId());
            produtoRepository.save(produto);
            return produtoEncontrado;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping
    public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);

        return produtoRepository.findAll(example);

    }

}
