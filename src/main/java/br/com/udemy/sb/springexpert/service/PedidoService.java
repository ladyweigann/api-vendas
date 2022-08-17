package br.com.udemy.sb.springexpert.service;

import br.com.udemy.sb.springexpert.domain.entity.Pedido;
import br.com.udemy.sb.springexpert.domain.enums.StatusPedido;
import br.com.udemy.sb.springexpert.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
