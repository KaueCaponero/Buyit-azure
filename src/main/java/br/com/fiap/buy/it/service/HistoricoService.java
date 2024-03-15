package br.com.fiap.buy.it.service;

import br.com.fiap.buy.it.dto.HistoricoDTO;
import br.com.fiap.buy.it.model.Historico;
import br.com.fiap.buy.it.repository.HistoricoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private CotacaoService cotacaoService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private UsuarioService usuarioService;

    public Page<Historico> listAll(Pageable pageRequest) {
        return historicoRepository.findAll(pageRequest);
    }

    public Historico findById(Long id) {
        Historico entity = findEntityById(id);
        return entity;
    }

    @Transactional
    public Historico create(HistoricoDTO newData) {
        Historico entity = convertToEntity(newData);
        Historico savedEntity = historicoRepository.save(entity);
        return savedEntity;
    }

    @Transactional
    public Historico update(Long id, HistoricoDTO updatedData) {
        findEntityById(id);
        updatedData.setId(id);
        Historico updatedEntity = convertToEntity(updatedData);    
        Historico savedEntity = historicoRepository.save(updatedEntity);
        return savedEntity;
    }
    
    @Transactional
    public void delete(Long id) {
        Historico entity = findEntityById(id);
        historicoRepository.delete(entity);
    }

    public Historico findEntityById(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "(" + getClass().getSimpleName() + ") - Historico não encontrado(a) por ID: " + id));
    }

    // private HistoricoDTO convertToDto(Historico entity) {
    //     HistoricoDTO dto = new HistoricoDTO();
    //     dto.setId(entity.getId());
    //     dto.setIdCotacao(entity.getCotacao() != null ? entity.getCotacao().getId() : null);
    //     dto.setIdFornecedor(entity.getFornecedor() != null ? entity.getFornecedor().getId() : null);
    //     dto.setIdStatus(entity.getStatus() != null ? entity.getStatus().getId() : null);
    //     dto.setRecusadoPorProduto(entity.getRecusadoPorProduto());
    //     dto.setRecusadoPorQuantidade(entity.getRecusadoPorQuantidade());
    //     dto.setRecusadoPorPreco(entity.getRecusadoPorPreco());
    //     dto.setRecusadoPorPrazo(entity.getRecusadoPorPrazo());
    //     dto.setDescricao(entity.getDescricao());
    //     dto.setData(entity.getData());
    //     dto.setValorOfertado(entity.getValorOfertado());
    //     return dto;
    // }

    private Historico convertToEntity(HistoricoDTO dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("(" + getClass().getSimpleName() + ") - HistoricoDTO não pode ser nulo.");
        }
        Historico entity;
        if (dto.getId() != null) {
            entity = findEntityById(dto.getId());
        } else {
            entity = new Historico();
        }
        if (dto.getIdCotacao() == null) {
            throw new IllegalArgumentException("(" + getClass().getSimpleName() + ") - ID Cotacao não pode ser nulo.");
        }
        if (dto.getIdFornecedor() == null) {
            throw new IllegalArgumentException("(" + getClass().getSimpleName() + ") - ID Fornecedor não pode ser nulo.");
        }
        if (dto.getIdStatus() == null) {
            throw new IllegalArgumentException("(" + getClass().getSimpleName() + ") - ID Status não pode ser nulo.");
        }
        entity.setCotacao(cotacaoService.findEntityById(dto.getIdCotacao()));
        entity.setFornecedor(usuarioService.findEntityById(dto.getIdFornecedor()));
        entity.setStatus(statusService.findEntityById(dto.getIdStatus()));
        entity.setRecusadoPorProduto(dto.getRecusadoPorProduto());
        entity.setRecusadoPorQuantidade(dto.getRecusadoPorQuantidade());
        entity.setRecusadoPorPreco(dto.getRecusadoPorPreco());
        entity.setRecusadoPorPrazo(dto.getRecusadoPorPrazo());
        entity.setDescricao(dto.getDescricao());
        entity.setData(dto.getData());
        entity.setValorOfertado(dto.getValorOfertado());
        return entity;
    }
}