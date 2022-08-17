package br.com.udemy.sb.springexpert.domain.repository;

import br.com.udemy.sb.springexpert.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}
