<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:public-template title="Login">

    <div class="login-card">
        <div class="login-header">
            <span class="logo-text">ReservasApp</span>
            <p>Bem-vindo de volta! Faça o login para continuar.</p>
        </div>

            <%-- Exibição de mensagens de feedback --%>
        <c:if test="${param.error != null}">
            <div class="form-error-global">E-mail ou senha inválidos.</div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="form-success-global">Você saiu do sistema com sucesso.</div>
        </c:if>
        <c:if test="${not empty mensagemSucesso}">
            <div class="form-success-global">${mensagemSucesso}</div>
        </c:if>

        <form action="<c:url value="/login"/>" method="post" class="form-main">
            <div class="form-group">
                <label for="username">E-mail</label>
                <input type="email" id="username" name="username" class="form-control" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Senha</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>
            <div class="form-actions" style="border-top: none; margin-top: 0.5rem;">
                <a href="<c:url value='/clientes/novo' />" class="btn btn-secondary">Realizar Cadastro</a>
                <button type="submit" class="btn btn-primary" style="flex-grow: 1;">Entrar</button>
            </div>
        </form>
    </div>

</layout:public-template>