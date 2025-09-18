<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="agendamento" type="com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.DetalhesAgendamentoDto"--%>

<layout:template title="Detalhes da Solicitação" activeMenu="inicio">
    <div class="content-card">
        <div class="card-header">
            <h1>Detalhes da Solicitação de Agendamento</h1>
        </div>
        <div class="card-body">
            <div class="form-main">
                <h3>Dados da Sala</h3>
                <div class="form-group">
                    <label>Sala</label>
                    <input type="text" class="form-control"
                           value="<c:out value="${agendamento.nomeSala}"/>" disabled>
                </div>
                <div class="form-group">
                    <label>Setor</label>
                    <input type="text" class="form-control"
                           value="<c:out value="${agendamento.nomeSetor}"/>" disabled>
                </div>
                <div class="form-group">
                    <label>Capacidade Máxima</label>
                    <input type="text" class="form-control"
                           value="${agendamento.capacidadeSala} pessoas" disabled>
                </div>

                <h3 style="margin-top: 1.5rem;">Dados do Agendamento</h3>
                <div class="form-group">
                    <label>Início do Agendamento</label>
                        <%-- Exibe a String de data já formatada que veio do DTO --%>
                    <input type="text" class="form-control" value="${agendamento.dataHoraInicio}"
                           disabled>
                </div>
                <div class="form-group">
                    <label>Fim do Agendamento</label>
                        <%-- Exibe a String de data já formatada que veio do DTO --%>
                    <input type="text" class="form-control" value="${agendamento.dataHoraFim}"
                           disabled>
                </div>
                <div class="form-group">
                    <label>Quantidade de Pessoas</label>
                    <input type="text" class="form-control" value="${agendamento.quantidadePessoas}"
                           disabled>
                </div>

                <div class="preco-calculado">
                    <div>Valor Total: <strong><fmt:formatNumber value="${agendamento.valorTotal}"
                                                                type="currency"/></strong></div>
                    <div>Valor do Sinal (50%): <strong><fmt:formatNumber
                            value="${agendamento.valorSinal}" type="currency"/></strong></div>
                </div>

                <div class="form-actions">
                    <a href="<c:url value='/' />" class="btn btn-secondary">Voltar ao Dashboard</a>

                        <%--
                          Formulário que será submetido pelo modal de confirmação.
                          A action aponta para a rota de cancelamento, passando o ID do agendamento.
                        --%>
                    <form id="cancelForm-${agendamento.agendamentoId}"
                          action="<c:url value='/agendamentos/cancelar/${agendamento.agendamentoId}' />"
                          method="post" style="display:inline;">
                        <button type="button" class="btn btn-danger-outline"
                                onclick="openDeleteModal('cancelForm-${agendamento.agendamentoId}', 'Deseja realmente cancelar esta solicitação de agendamento?')">
                            Cancelar Solicitação
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</layout:template>