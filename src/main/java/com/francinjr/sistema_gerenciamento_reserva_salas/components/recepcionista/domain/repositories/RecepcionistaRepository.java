package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {

    /**
     * Busca todos os recepcionistas de forma paginada, trazendo todos os relacionamentos
     * em uma única consulta otimizada para evitar o problema N+1.
     */
    @Query(value = "SELECT r FROM Recepcionista r " +
            "JOIN FETCH r.pessoaFisica " +
            "JOIN FETCH r.setor " +
            "JOIN FETCH r.usuario",
            countQuery = "SELECT COUNT(r) FROM Recepcionista r")
    Page<Recepcionista> findAllWithDetails(Pageable pageable);

    /**
     * Busca recepcionistas pelo nome da PessoaFisica associada, de forma paginada e otimizada.
     */
    @Query(value = "SELECT r FROM Recepcionista r " +
            "JOIN FETCH r.pessoaFisica pf " +
            "JOIN FETCH r.setor s " +
            "JOIN FETCH r.usuario u " +
            "WHERE lower(pf.nome) LIKE lower(concat('%', :nome, '%'))",
            countQuery = "SELECT COUNT(r) FROM Recepcionista r JOIN r.pessoaFisica pf WHERE lower(pf.nome) LIKE lower(concat('%', :nome, '%'))")
    Page<Recepcionista> findByPessoaFisicaNomeContainingIgnoreCaseWithDetails(@Param("nome") String nome, Pageable pageable);

    /**
     * Busca um único recepcionista por ID, trazendo todos os detalhes.
     */
    @Query("SELECT r FROM Recepcionista r JOIN FETCH r.pessoaFisica JOIN FETCH r.setor JOIN FETCH r.usuario WHERE r.id = :id")
    Optional<Recepcionista> findByIdWithDetails(@Param("id") Long id);
}