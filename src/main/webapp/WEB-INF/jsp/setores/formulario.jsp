<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<layout:template title="${empty setorId ? 'Novo Setor' : 'Editar Setor'}" activeMenu="setores">
    <div class="content-card">
        <div class="card-header"><h1>${empty setorId ? 'Adicionar Novo Setor' : 'Editar Setor'}</h1>
        </div>
        <div class="card-body">
            <c:url var="postUrl"
                   value="${empty setorId ? '/setores/salvar' : '/setores/atualizar/'.concat(setorId)}"/>
            <form:form modelAttribute="setorDto" action="${postUrl}" method="post"
                       cssClass="form-main">
                <div class="form-group">
                    <form:label path="nome">Nome do Setor</form:label>
                    <form:input path="nome" cssClass="form-control"
                                placeholder="Ex: Tecnologia da Informação"/>
                    <form:errors path="nome" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="descricao">Descrição</form:label>
                    <form:textarea path="descricao" cssClass="form-control" rows="4"
                                   placeholder="Uma breve descrição sobre o setor..."/>
                    <form:errors path="descricao" cssClass="form-error"/>
                </div>
                <div class="form-actions">
                    <button type="submit"
                            class="btn btn-primary">${empty setorId ? 'Salvar Setor' : 'Atualizar Setor'}</button>
                    <a href="<c:url value='/setores/listar' />"
                       class="btn btn-secondary">Cancelar</a>
                </div>
            </form:form>
        </div>
    </div>
</layout:template>