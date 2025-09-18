<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--@elvariable id="salaDto" type="..."--%>
<%--@elvariable id="salaId" type="java.lang.Long"--%>
<%--@elvariable id="setores" type="java.util.List<...>"--%>

<layout:template title="${empty salaId ? 'Nova Sala' : 'Editar Sala'}" activeMenu="salas">
    <div class="content-card">
        <div class="card-header"><h1>${empty salaId ? 'Adicionar Nova Sala' : 'Editar Sala'}</h1></div>
        <div class="card-body">
            <c:url var="postUrl" value="${empty salaId ? '/salas/salvar' : '/salas/atualizar/'.concat(salaId)}" />

            <form:form modelAttribute="salaDto" action="${postUrl}" method="post" cssClass="form-main">

                <div class="form-group">
                    <form:label path="nome">Nome da Sala</form:label>
                    <form:input path="nome" cssClass="form-control" placeholder="Ex: Sala de Reunião Principal"/>
                    <form:errors path="nome" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="preco">Preço por Período (R$)</form:label>
                    <form:input type="number" step="0.01" path="preco" cssClass="form-control" placeholder="Ex: 150.50"/>
                    <form:errors path="preco" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="capacidadeMaxima">Capacidade Máxima (Pessoas)</form:label>
                    <form:input type="number" path="capacidadeMaxima" cssClass="form-control" placeholder="Ex: 25"/>
                    <form:errors path="capacidadeMaxima" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="descricao">Descrição</form:label>
                    <form:textarea path="descricao" cssClass="form-control" rows="4" placeholder="Uma breve descrição sobre a sala..."/>
                    <form:errors path="descricao" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="setorId">Setor</form:label>
                    <form:select path="setorId" cssClass="form-control">
                        <form:option value="" label="-- Selecione um Setor --"/>
                        <form:options items="${setores}" itemValue="id" itemLabel="nome"/>
                    </form:select>
                    <form:errors path="setorId" cssClass="form-error"/>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">${empty salaId ? 'Salvar Sala' : 'Atualizar Sala'}</button>
                    <a href="<c:url value='/salas/listar' />" class="btn btn-secondary">Cancelar</a>
                </div>
            </form:form>
        </div>
    </div>
</layout:template>