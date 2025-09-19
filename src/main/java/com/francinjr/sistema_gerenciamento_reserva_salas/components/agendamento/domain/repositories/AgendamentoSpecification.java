package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class AgendamentoSpecification {

    public static Specification<Agendamento> comDataFinalizacaoEntre(LocalDateTime inicio, LocalDateTime fim) {
        return (root, query, cb) -> {
            if (inicio == null && fim == null) return null;
            if (inicio == null) return cb.lessThanOrEqualTo(root.get("dataHoraFinalizacao"), fim);
            if (fim == null) return cb.greaterThanOrEqualTo(root.get("dataHoraFinalizacao"), inicio);
            return cb.between(root.get("dataHoraFinalizacao"), inicio, fim);
        };
    }

    public static Specification<Agendamento> comNomeDeSetor(String nomeSetor) {
        return (root, query, cb) -> {
            if (nomeSetor == null || nomeSetor.isBlank()) return null;
            Join<Agendamento, Setor> setorJoin = root.join("sala").join("setor");
            return cb.like(cb.lower(setorJoin.get("nome")), "%" + nomeSetor.toLowerCase() + "%");
        };
    }

    public static Specification<Agendamento> doCliente(Cliente cliente) {
        return (root, query, cb) -> cb.equal(root.get("cliente"), cliente);
    }

    public static Specification<Agendamento> doSetor(Setor setor) {
        return (root, query, cb) -> cb.equal(root.join("sala").get("setor"), setor);
    }
}
