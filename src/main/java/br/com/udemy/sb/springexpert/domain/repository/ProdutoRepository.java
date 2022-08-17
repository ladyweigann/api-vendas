package br.com.udemy.sb.springexpert.domain.repository;

import br.com.udemy.sb.springexpert.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Integer> {
}
