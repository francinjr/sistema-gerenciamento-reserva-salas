<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<layout:template title="Relatório de Agendamentos" activeMenu="relatorios">
    <div class="content-card">
        <div class="card-header">
            <h1>Relatório de Agendamentos Finalizados</h1>
        </div>
        <div class="card-body">
            <form:form modelAttribute="filtro" action="/relatorios/agendamentos" method="get"
                       cssClass="form-main filter-form">
                <div class="form-group">
                    <form:label path="dataInicio">Data de Início</form:label>
                    <form:input type="date" path="dataInicio" cssClass="form-control"/>
                </div>
                <div class="form-group">
                    <form:label path="dataFim">Data de Fim</form:label>
                    <form:input type="date" path="dataFim" cssClass="form-control"/>
                </div>

                <%-- O filtro de setor só aparece para Admin e Recepcionista --%>
                <sec:authorize access="hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONISTA')">
                    <div class="form-group">
                        <form:label path="nomeSetor">Nome do Setor</form:label>
                        <form:input path="nomeSetor" cssClass="form-control"/>
                    </div>
                </sec:authorize>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Filtrar</button>
                </div>
            </form:form>

            <c:if test="${empty relatorio}">
                <p class="no-data" style="margin-top: 2rem;">Nenhum agendamento finalizado
                    encontrado para os filtros selecionados.</p>
            </c:if>

            <c:forEach var="setor" items="${relatorio}">
                <div class="setor-card" style="margin-top: 2rem;">
                    <div class="setor-card-header report-header">
                        <h2>Setor: <c:out value="${setor.nome}"/></h2>
                        <strong>
                            <sec:authorize
                                    access="hasRole('ROLE_CLIENTE')">Gasto Total:</sec:authorize>
                            <sec:authorize
                                    access="hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONISTA')">Faturamento Total:</sec:authorize>
                            <fmt:formatNumber value="${setor.faturamentoTotal.valor}"
                                              type="currency"/>
                        </strong>
                    </div>
                    <div class="table-wrapper">
                        <table>
                            <thead>
                            <tr>
                                <th>Sala</th>
                                <th>
                                    <sec:authorize
                                            access="hasRole('ROLE_CLIENTE')">Gasto</sec:authorize>
                                    <sec:authorize
                                            access="hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONISTA')">Faturamento</sec:authorize>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="sala" items="${setor.salas}">
                                <tr>
                                    <td><c:out value="${sala.nome}"/></td>
                                    <td><fmt:formatNumber value="${sala.faturamento.valor}"
                                                          type="currency"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</layout:template>