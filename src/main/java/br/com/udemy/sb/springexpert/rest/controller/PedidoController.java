package br.com.udemy.sb.springexpert.rest.controller;

import br.com.udemy.sb.springexpert.domain.entity.ItemPedido;
import br.com.udemy.sb.springexpert.domain.entity.Pedido;
import br.com.udemy.sb.springexpert.domain.enums.StatusPedido;
import br.com.udemy.sb.springexpert.rest.dto.AtualizacaoStatusPedidoDTO;
import br.com.udemy.sb.springexpert.rest.dto.InformacaoItemPedidoDTO;
import br.com.udemy.sb.springexpert.rest.dto.InformacoesPedidoDTO;
import br.com.udemy.sb.springexpert.rest.dto.PedidoDTO;
import br.com.udemy.sb.springexpert.service.PedidoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService pedidoService;


    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoService.salvar(pedidoDTO);
        return pedido.getId();

    }

    @GetMapping("/{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return pedidoService.obterPedidoCompleto(id)
                .map(this::converter)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado"));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@RequestBody AtualizacaoStatusPedidoDTO dto, @PathVariable Integer id){
        String novoStatus = dto.getNovoStatus();
        pedidoService.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
    }
    private InformacoesPedidoDTO converter(Pedido pedido) {
       return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if(CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(item -> InformacaoItemPedidoDTO
                .builder()
                .descricaoProduto(item.getProduto().getDescricao())
                .precoUnitario(item.getProduto().getPreco())
                .quantidade(item.getQuantidade()).build()).collect(Collectors.toList());
    }
}
