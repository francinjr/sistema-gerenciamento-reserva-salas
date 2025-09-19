<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>


<layout:template title="Painel da Recepcionista" activeMenu="painel">

    <div class="content-card">
        <div class="card-header">
            <h1>Agendamentos Ativos</h1>
        </div>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Cliente</th>
                    <th>Sala</th>
                    <th>Início</th>
                    <th>Fim</th>
                    <th>Status</th>
                    <th>Valor a Receber</th>
                    <th style="width: 1%; white-space: nowrap;">Ações</th>
                </tr>
                </thead>
                <tbody>
                    <%-- Verifica se a lista de agendamentos está vazia --%>
                <c:if test="${empty agendamentosPage.content}">
                    <tr>
                        <td colspan="7" style="text-align: center; color: #6c757d;">
                            Nenhum agendamento ativo no momento.
                        </td>
                    </tr>
                </c:if>

                    <%-- Itera sobre a lista de agendamentos para os exibir --%>
                <c:forEach var="agendamento" items="${agendamentosPage.content}">
                    <tr>
                        <td><c:out value="${agendamento.nomeCliente}"/></td>
                        <td><c:out value="${agendamento.nomeSala}"/></td>
                            <%-- Exibe a String de data já formatada que veio do DTO --%>
                        <td><c:out value="${agendamento.dataHoraInicio}"/></td>
                        <td><c:out value="${agendamento.dataHoraFim}"/></td>
                        <td>
                                <%-- Exibe o status com uma "badge" colorida --%>
                            <span class="status-badge status-${agendamento.status.name().toLowerCase()}">
                                    <c:out value="${agendamento.status.name()}"/>
                                </span>
                        </td>
                        <td>
                                <%-- Mostra o valor a receber apenas se o agendamento estiver confirmado --%>
                            <c:if test="${agendamento.status == 'CONFIRMADO'}">
                                <fmt:formatNumber value="${agendamento.valorFinalizacao}" type="currency"/>
                            </c:if>
                        </td>
                        <td class="actions-cell">

                                <%-- Lógica condicional para exibir as ações corretas para cada status --%>
                            <c:choose>
                                <c:when test="${agendamento.status == 'SOLICITADO'}">
                                    <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/confirmar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-primary">Confirmar</button></form>
                                    <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/recusar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-danger-outline">Recusar</button></form>
                                </c:when>
                                <c:when test="${agendamento.status == 'CONFIRMADO'}">
                                    <form action="<c:url value='/painel-recepcionista/agendamentos/${agendamento.id}/finalizar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-success">Finalizar</button></form>
                                </c:when>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

            <%-- A secção do rodapé com a paginação --%>
        <div class="card-footer">
            <div class="items-per-page">
                <span>Itens por página: ${agendamentosPage.size}</span>
            </div>
            <div class="page-info">
                <span>Página ${agendamentosPage.number + 1} de ${agendamentosPage.totalPages > 0 ? agendamentosPage.totalPages : 1}</span>
            </div>
            <div class="pagination-nav">
                <c:url var="prevUrl" value="/painel-recepcionista/dashboard">
                    <c:param name="page" value="${agendamentosPage.number - 1}"/>
                </c:url>
                <a href="${prevUrl}" class="btn-nav ${agendamentosPage.first ? 'disabled' : ''}"><i class="fa-solid fa-chevron-left"></i></a>

                <c:url var="nextUrl" value="/painel-recepcionista/dashboard">
                    <c:param name="page" value="${agendamentosPage.number + 1}"/>
                </c:url>
                <a href="${nextUrl}" class="btn-nav ${agendamentosPage.last ? 'disabled' : ''}"><i class="fa-solid fa-chevron-right"></i></a>
            </div>
        </div>
    </div>
</layout:template>