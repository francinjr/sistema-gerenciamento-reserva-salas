<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<layout:template title="Formulário de Setor" activeMenu="setores">

    <%-- O conteúdo abaixo é injetado no <jsp:doBody /> do layout --%>
    <div class="content-card">
        <div class="card-header">
            <c:choose>
                <c:when test="${empty setorId}">
                    <h1>Adicionar Novo Setor</h1>
                </c:when>
                <c:otherwise>
                    <h1>Editar Setor</h1>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card-body">
                <%--
                  O modelAttribute="setorDto" conecta este formulário ao objeto passado pelo Controller.
                  Isso preenche os campos automaticamente (no caso de edição) e permite exibir erros.
                --%>
            <form:form modelAttribute="setorDto" action="/setores/salvar" method="post" cssClass="form-main">

                <div class="form-group">
                    <form:label path="nome">Nome do Setor</form:label>
                    <form:input path="nome" cssClass="form-control" placeholder="Ex: Tecnologia da Informação"/>

                        <%--
                          Esta tag exibirá automaticamente qualquer erro associado ao campo 'nome',
                          seja de validação (@NotBlank) ou de duplicação (do nosso Translator).
                        --%>
                    <form:errors path="nome" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="descricao">Descrição</form:label>
                    <form:textarea path="descricao" cssClass="form-control" rows="4" placeholder="Uma breve descrição sobre o setor..."/>
                    <form:errors path="descricao" cssClass="form-error"/>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Salvar Setor</button>
                    <a href="<c:url value='/setores/listar' />" class="btn btn-secondary">Cancelar</a>
                </div>

            </form:form>
        </div>
    </div>

</layout:template>