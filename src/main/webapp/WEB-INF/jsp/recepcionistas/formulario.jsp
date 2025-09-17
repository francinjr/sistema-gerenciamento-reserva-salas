<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--@elvariable id="recepcionistaDto" type="..."--%>
<%--@elvariable id="setores" type="java.util.List<...>"--%>

<layout:template title="${empty recepcionistaId ? 'Novo Recepcionista' : 'Editar Recepcionista'}" activeMenu="recepcionistas">
    <div class="content-card">
        <div class="card-header"><h1>${empty recepcionistaId ? 'Adicionar Novo Recepcionista' : 'Editar Recepcionista'}</h1></div>
        <div class="card-body">
            <c:url var="postUrl" value="${empty recepcionistaId ? '/recepcionistas/salvar' : '/recepcionistas/atualizar/'.concat(recepcionistaId)}" />

            <form:form modelAttribute="recepcionistaDto" action="${postUrl}" method="post" cssClass="form-main">

                <h3>Dados Pessoais</h3>
                <div class="form-group">
                    <form:label path="pessoaFisica.nome">Nome Completo</form:label>
                    <form:input path="pessoaFisica.nome" cssClass="form-control" />
                    <form:errors path="pessoaFisica.nome" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="pessoaFisica.cpf">CPF (apenas números)</form:label>
                        <%-- ✅ ALTERADO: Adicionado 'maxlength' e 'disabled' condicional --%>
                    <form:input path="pessoaFisica.cpf"
                                cssClass="form-control"
                                maxlength="11"
                                disabled="${not empty recepcionistaId}" />
                    <form:errors path="pessoaFisica.cpf" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="pessoaFisica.telefone">Telefone</form:label>
                    <form:input path="pessoaFisica.telefone" cssClass="form-control" />
                    <form:errors path="pessoaFisica.telefone" cssClass="form-error"/>
                </div>

                <h3 style="margin-top: 1.5rem;">Dados de Acesso</h3>
                <div class="form-group">
                    <form:label path="usuario.email">E-mail</form:label>
                    <form:input path="usuario.email" cssClass="form-control" type="email"/>
                    <form:errors path="usuario.email" cssClass="form-error"/>
                </div>
                <div class="form-group">
                    <form:label path="usuario.senha">Senha</form:label>
                        <%-- ✅ ALTERADO: Adicionado 'disabled' condicional --%>
                    <form:input path="usuario.senha"
                                cssClass="form-control"
                                type="password"
                                placeholder="${not empty recepcionistaId ? 'Campo travado por segurança' : ''}"
                                disabled="${not empty recepcionistaId}" />
                    <form:errors path="usuario.senha" cssClass="form-error"/>
                </div>

                <h3 style="margin-top: 1.5rem;">Dados do Cargo</h3>
                <div class="form-group">
                    <form:label path="setorId">Setor</form:label>
                    <form:select path="setorId" cssClass="form-control">
                        <form:option value="" label="-- Selecione um Setor --"/>
                        <form:options items="${setores}" itemValue="id" itemLabel="nome"/>
                    </form:select>
                    <form:errors path="setorId" cssClass="form-error"/>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">${empty recepcionistaId ? 'Salvar Recepcionista' : 'Atualizar Recepcionista'}</button>
                    <a href="<c:url value='/recepcionistas/listar' />" class="btn btn-secondary">Cancelar</a>
                </div>
            </form:form>
        </div>
    </div>
</layout:template>