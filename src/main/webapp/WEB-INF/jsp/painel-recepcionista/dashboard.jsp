<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:template title="Painel da Recepcionista" activeMenu="painel">

    <div class="setor-status-panel">
        <div class="status-info">
            <h2>Setor: <c:out value="${setor.nome}"/></h2>
            <span class="status-badge status-${setor.status.name().toLowerCase()}">${setor.status.name()}</span>
        </div>
        <div class="caixa-info">
            <span>Caixa</span>
            <strong><fmt:formatNumber value="${caixaDoDia.valor}" type="currency"/></strong>
        </div>
        <div class="status-actions">
            <c:choose>
                <c:when test="${setor.status == 'FECHADO'}">
                    <form action="<c:url value='/painel-recepcionista/setor/abrir' />" method="post">
                        <button type="submit" class="btn btn-success">Abrir Setor</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="<c:url value='/painel-recepcionista/setor/fechar' />" method="post">
                        <button type="submit" class="btn btn-danger">Fechar Setor</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="content-card" style="margin-top: 2rem;">
        <div class="card-header"><h1>Solicitações Pendentes</h1></div>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr><th>Cliente</th><th>Sala</th><th>Início</th><th>Fim</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:if test="${empty solicitadosPage.content}">
                    <tr><td colspan="5" style="text-align: center; color: #6c757d;">Nenhuma solicitação pendente.</td></tr>
                </c:if>
                <c:forEach var="agendamento" items="${solicitadosPage.content}">
                    <tr>
                        <td><c:out value="${agendamento.nomeCliente}"/></td>
                        <td><c:out value="${agendamento.nomeSala}"/></td>
                        <td><c:out value="${agendamento.dataHoraInicio}"/></td>
                        <td><c:out value="${agendamento.dataHoraFim}"/></td>
                        <td class="actions-cell">
                            <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/confirmar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-primary">Confirmar</button></form>
                            <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/recusar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-danger-outline">Recusar</button></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="card-footer">
            <div class="items-per-page"><span>Itens por página: ${solicitadosPage.size}</span></div>
            <div class="page-info"><span>Página ${solicitadosPage.number + 1} de ${solicitadosPage.totalPages > 0 ? solicitadosPage.totalPages : 1}</span></div>
            <div class="pagination-nav">
                <c:url var="prevUrlSol" value="/painel-recepcionista/dashboard"><c:param name="solPage" value="${solicitadosPage.number - 1}"/><c:param name="confPage" value="${confirmadosPage.number}"/></c:url>
                <a href="${prevUrlSol}" class="${solicitadosPage.first ? 'disabled' : ''}"><i class="fa-solid fa-chevron-left"></i></a>
                <c:url var="nextUrlSol" value="/painel-recepcionista/dashboard"><c:param name="solPage" value="${solicitadosPage.number + 1}"/><c:param name="confPage" value="${confirmadosPage.number}"/></c:url>
                <a href="${nextUrlSol}" class="${solicitadosPage.last ? 'disabled' : ''}"><i class="fa-solid fa-chevron-right"></i></a>
            </div>
        </div>
    </div>

    <div class="content-card" style="margin-top: 2rem;">
        <div class="card-header"><h1>Agendamentos Confirmados</h1></div>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr><th>Cliente</th><th>Sala</th><th>Início</th><th>Fim</th><th>Valor a Receber</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:if test="${empty confirmadosPage.content}">
                    <tr><td colspan="6" style="text-align: center; color: #6c757d;">Nenhum agendamento confirmado.</td></tr>
                </c:if>
                <c:forEach var="agendamento" items="${confirmadosPage.content}">
                    <tr>
                        <td><c:out value="${agendamento.nomeCliente}"/></td>
                        <td><c:out value="${agendamento.nomeSala}"/></td>
                        <td><c:out value="${agendamento.dataHoraInicio}"/></td>
                        <td><c:out value="${agendamento.dataHoraFim}"/></td>
                        <td><fmt:formatNumber value="${agendamento.valorFinalizacao}" type="currency"/></td>
                        <td class="actions-cell">
                            <form action="<c:url value='/painel-recepcionista/agendamentos/${agendamento.id}/finalizar' />" method="post" style="display:inline;"><button type="submit" class="btn btn-success">Finalizar</button></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="card-footer">
            <div class="items-per-page"><span>Itens por página: ${confirmadosPage.size}</span></div>
            <div class="page-info"><span>Página ${confirmadosPage.number + 1} de ${confirmadosPage.totalPages > 0 ? confirmadosPage.totalPages : 1}</span></div>
            <div class="pagination-nav">
                <c:url var="prevUrlConf" value="/painel-recepcionista/dashboard"><c:param name="solPage" value="${solicitadosPage.number}"/><c:param name="confPage" value="${confirmadosPage.number - 1}"/></c:url>
                <a href="${prevUrlConf}" class="${confirmadosPage.first ? 'disabled' : ''}"><i class="fa-solid fa-chevron-left"></i></a>
                <c:url var="nextUrlConf" value="/painel-recepcionista/dashboard"><c:param name="solPage" value="${solicitadosPage.number}"/><c:param name="confPage" value="${confirmadosPage.number + 1}"/></c:url>
                <a href="${nextUrlConf}" class="${confirmadosPage.last ? 'disabled' : ''}"><i class="fa-solid fa-chevron-right"></i></a>
            </div>
        </div>
    </div>
</layout:template>