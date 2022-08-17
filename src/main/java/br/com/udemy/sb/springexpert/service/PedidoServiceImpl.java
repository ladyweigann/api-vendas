package br.com.udemy.sb.springexpert.service;

import br.com.udemy.sb.springexpert.domain.entity.Cliente;
import br.com.udemy.sb.springexpert.domain.entity.ItemPedido;
import br.com.udemy.sb.springexpert.domain.entity.Pedido;
import br.com.udemy.sb.springexpert.domain.entity.Produto;
import br.com.udemy.sb.springexpert.domain.enums.StatusPedido;
import br.com.udemy.sb.springexpert.domain.repository.ClienteRepository;
import br.com.udemy.sb.springexpert.domain.repository.ItemPedidoRepository;
import br.com.udemy.sb.springexpert.domain.repository.PedidoRepository;
import br.com.udemy.sb.springexpert.domain.repository.ProdutoRepository;
import br.com.udemy.sb.springexpert.exception.PedidoNaoEncontradoException;
import br.com.udemy.sb.springexpert.exception.RegraNegocioException;
import br.com.udemy.sb.springexpert.rest.dto.ItemPedidoDTO;
import br.com.udemy.sb.springexpert.rest.dto.PedidoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {

        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = converterItens(pedido, dto.getItens());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);

        return pedido;

    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(p -> {
                    p.setStatus(statusPedido);
                    return pedidoRepository.save(p);
                })
                .orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if(itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }

        return itens.stream().map(dto -> {
            Integer idProduto = dto.getProduto();

            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);

            return itemPedido;
        }).collect(Collectors.toList());
    }
}
