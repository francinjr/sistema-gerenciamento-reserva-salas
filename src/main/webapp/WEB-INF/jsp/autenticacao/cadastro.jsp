<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<layout:public-template title="Cadastro de Cliente">
  <div class="login-card">
    <div class="card-header" style="border: none; padding-bottom: 0;">
      <h1>Cadastro de Novo Cliente</h1>
    </div>
    <div class="card-body" style="padding-top: 1.5rem;">
      <form:form modelAttribute="clienteDto" action="/clientes" method="post" cssClass="form-main">
        <h3 style="font-size: 1rem; color: var(--text-secondary); border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1rem;">Dados Pessoais</h3>
        <div class="form-group">
          <form:label path="pessoaFisica.nome">Nome Completo</form:label>
          <form:input path="pessoaFisica.nome" cssClass="form-control" />
          <form:errors path="pessoaFisica.nome" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <form:label path="pessoaFisica.cpf">CPF (apenas números)</form:label>
          <form:input path="pessoaFisica.cpf" cssClass="form-control" />
          <form:errors path="pessoaFisica.cpf" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <form:label path="pessoaFisica.telefone">Telefone</form:label>
          <form:input path="pessoaFisica.telefone" cssClass="form-control" />
          <form:errors path="pessoaFisica.telefone" cssClass="form-error"/>
        </div>

        <h3 style="margin-top: 1.5rem; font-size: 1rem; color: var(--text-secondary); border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1rem;">Dados de Acesso</h3>
        <div class="form-group">
          <form:label path="usuario.email">E-mail</form:label>
          <form:input path="usuario.email" cssClass="form-control" type="email"/>
          <form:errors path="usuario.email" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <form:label path="usuario.senha">Senha</form:label>
          <form:input path="usuario.senha" cssClass="form-control" type="password"/>
          <form:errors path="usuario.senha" cssClass="form-error"/>
        </div>
        <div class="form-actions" style="margin-top: 0.5rem;">
          <a href="<c:url value='/login' />" class="btn btn-secondary">Voltar para o Login</a>
          <button type="submit" class="btn btn-primary">Finalizar Cadastro</button>
        </div>
      </form:form>
    </div>
  </div>
</layout:public-template>