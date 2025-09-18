<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Sistema de Reservas</title>

    <%-- Estilos globais (para botões, inputs, etc.) --%>
    <link rel="stylesheet" href="<c:url value="/css/style.css" />">
    <%-- Estilos específicos para o layout de login/cadastro --%>
    <link rel="stylesheet" href="<c:url value="/css/login.css" />">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
</head>
<body class="public-page-body">

<div class="public-container">
    <%-- O conteúdo da página (login ou cadastro) será injetado aqui --%>
    <jsp:doBody/>
</div>

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script>
  function showToast(message, type) {
    let backgroundColor = (type === 'success')
        ? "linear-gradient(to right, #00b09b, #96c93d)"
        : "linear-gradient(to right, #ff5f6d, #ffc371)";
    Toastify({
      text: message, duration: 5000, close: true, gravity: "top",
      position: "right", stopOnFocus: true, style: { background: backgroundColor }
    }).showToast();
  }
  <c:if test="${not empty mensagemSucesso}">showToast('${mensagemSucesso}', 'success');</c:if>
</script>
</body>
</html>