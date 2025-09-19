package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Dinheiro;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAgendamento status;

    @Column(name = "quantidade_pessoas", nullable = false)
    private Integer quantidadePessoas;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "valor_total", nullable = false))
    private Dinheiro valorTotal;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "valor_sinal", nullable = false))
    private Dinheiro valorSinal;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "valor_finalizacao", nullable = false))
    private Dinheiro valorFinalizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false, foreignKey = @ForeignKey(name = "fk_agendamento_sala"))
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_agendamento_cliente"))
    private Cliente cliente;

    @Column(name = "data_hora_finalizacao")
    private LocalDateTime dataHoraFinalizacao;

    public Agendamento(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Sala sala,
            Cliente cliente, Integer quantidadePessoas) {
        validarDatas(dataHoraInicio, dataHoraFim);
        validarSala(sala);
        validarCliente(cliente);
        validarCapacidade(quantidadePessoas, sala);

        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.sala = sala;
        this.cliente = cliente;
        this.quantidadePessoas = quantidadePessoas;
        this.status = StatusAgendamento.SOLICITADO;

        // Calcula os valores no momento da criação
        this.calcularValores();
    }


    public void confirmar() {
        if (this.status != StatusAgendamento.SOLICITADO) {
            throw new DominioException(
                    "Apenas agendamentos com status 'SOLICITADO' podem ser confirmados.");
        }
        this.status = StatusAgendamento.CONFIRMADO;
    }

    /*
    Recusar e cancelar podem parecer redudantes, mas essa separação tem um bom motivo, se trata do fato
    de que recusar é uma operação do ator Recpecionista, enquanto que cancelar representa uma operação
    do ator Cliente, por isso os metodos são segregados, caso haja mudanças na lógica envolvendo o cancelamento
    de um agendamento, basta adicionar a lógica adequada no repsectivo lugar, dessa forma evita problemas de
    incosistência.
     */
    public void recusar() {
        mudarStatusParaCancelado();
    }

    public void cancelar() {
        mudarStatusParaCancelado();
    }

    public void finalizar() {
        if (this.status != StatusAgendamento.CONFIRMADO) {
            throw new DominioException("Apenas agendamentos com status 'CONFIRMADO' podem ser finalizados.");
        }
        this.status = StatusAgendamento.FINALIZADO;
        this.dataHoraFinalizacao = LocalDateTime.now();
    }

    public void confirmarParaAgendamentoInstantenio() {
        this.status = StatusAgendamento.CONFIRMADO;
    }

    public boolean pertenceAoCliente(Cliente cliente) {
        return this.cliente != null && this.cliente.getId().equals(cliente.getId());
    }


    private void mudarStatusParaCancelado() {
        if (this.status != StatusAgendamento.SOLICITADO) {
            throw new DominioException(
                    "Apenas agendamentos com status 'SOLICITADO' podem ser alterados para 'CANCELADO'.");
        }
        this.status = StatusAgendamento.CANCELADO;
    }

    private void calcularValores() {
        Duration duration = Duration.between(this.dataHoraInicio, this.dataHoraFim);
        long minutos = duration.toMinutes();

        if (minutos <= 0) {
            this.valorTotal = new Dinheiro(0.0);
        } else {
            double horas = (double) minutos / 60.0;
            // Usa o metodo 'multiplicar' do VO Dinheiro para calcular o valor total
            this.valorTotal = this.sala.getPreco().multiplicar(horas);
        }

        // Usa os métodos do VO Dinheiro para os outros cálculos
        this.valorSinal = this.valorTotal.multiplicar(0.5);
        this.valorFinalizacao = this.valorTotal.subtrair(this.valorSinal);
    }

    private void validarDatas(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new DominioException("As datas de início e fim são obrigatórias.");
        }
        if (fim.isBefore(inicio) || fim.isEqual(inicio)) {
            throw new DominioException(
                    "A data/hora de fim deve ser posterior à data/hora de início.");
        }
    }

    private void validarSala(Sala sala) {
        if (sala == null || sala.getId() == null) {
            throw new DominioException("O agendamento deve estar associado a uma sala válida.");
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new DominioException("O agendamento deve estar associado a um cliente válido.");
        }
    }

    private void validarCapacidade(Integer quantidadePessoas, Sala sala) {
        if (quantidadePessoas == null || quantidadePessoas <= 0) {
            throw new DominioException("A quantidade de pessoas deve ser maior que zero.");
        }
        if (quantidadePessoas > sala.getCapacidadeMaxima()) {
            throw new DominioException(
                    "A quantidade de pessoas excede a capacidade máxima da sala ("
                            + sala.getCapacidadeMaxima() + ").");
        }
    }
}