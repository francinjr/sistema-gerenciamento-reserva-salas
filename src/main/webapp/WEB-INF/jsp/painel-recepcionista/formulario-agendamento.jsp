<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<layout:template title="Agendamento Instantâneo" activeMenu="agendar_sala">
    <div class="content-card">
        <div class="card-header"><h1>Agendamento Instantâneo: ${sala.nome}</h1></div>
        <div class="card-body">
            <div class="sala-info">
                <p><strong>Capacidade:</strong> ${sala.capacidadeMaxima} pessoas</p>
                <p><strong>Preço por Hora:</strong> <fmt:formatNumber value="${sala.preco.valor}" type="currency"/></p>
            </div>
            <form:form modelAttribute="agendamentoDto" action="/painel-recepcionista/agendamentos/salvar" method="post" cssClass="form-main">
                <form:hidden path="salaId" />
                <form:errors path="*" cssClass="form-error-global" element="div" />

                <div class="form-group">
                    <form:label path="clienteId">Cliente</form:label>
                    <form:select path="clienteId" cssClass="form-control">
                        <form:option value="" label="-- Selecione um Cliente --"/>
                        <c:forEach var="cliente" items="${clientes}">
                            <%-- A tag <form:options> é mais limpa, mas <c:forEach> também funciona bem --%>
                            <option value="${cliente.id}">${cliente.pessoaFisica.nome}</option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="clienteId" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="dataHoraInicio">Início do Agendamento</form:label>
                    <form:input type="datetime-local" path="dataHoraInicio" cssClass="form-control"/>
                    <form:errors path="dataHoraInicio" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="dataHoraFim">Fim do Agendamento</form:label>
                    <form:input type="datetime-local" path="dataHoraFim" cssClass="form-control"/>
                    <form:errors path="dataHoraFim" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="quantidadePessoas">Quantidade de Pessoas</form:label>
                    <form:input type="number" path="quantidadePessoas" cssClass="form-control"/>
                    <form:errors path="quantidadePessoas" cssClass="form-error"/>
                </div>

                <div class="form-actions">
                    <a href="<c:url value='/painel-recepcionista/agendar-sala' />" class="btn btn-secondary">Cancelar</a>
                    <button type="submit" class="btn btn-primary">Confirmar Agendamento</button>
                </div>
            </form:form>
        </div>
    </div>
</layout:template>