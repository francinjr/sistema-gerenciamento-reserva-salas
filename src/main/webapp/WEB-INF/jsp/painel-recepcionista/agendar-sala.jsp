<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:template title="Agendar Sala" activeMenu="agendar_sala">
    <div class="content-card">
        <div class="card-header">
            <h1>Agendar Sala - Setor: <c:out value="${setor.nome}"/></h1>
        </div>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nome da Sala</th>
                    <th>Capacidade</th>
                    <th>Preço por Hora</th>
                    <th style="width: 1%; white-space: nowrap;">Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty salas}">
                    <tr><td colspan="4" style="text-align: center; color: #6c757d;">Nenhuma sala encontrada neste setor.</td></tr>
                </c:if>
                <c:forEach var="sala" items="${salas}">
                    <tr>
                        <td><c:out value="${sala.nome}"/></td>
                        <td><c:out value="${sala.capacidadeMaxima}"/> pessoas</td>
                        <td><fmt:formatNumber value="${sala.preco}" type="currency"/></td>
                        <td class="actions-cell">
                            <a href="<c:url value='/painel-recepcionista/agendamentos/novo/${sala.id}'/>" class="btn btn-primary">Agendar Agora</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</layout:template>