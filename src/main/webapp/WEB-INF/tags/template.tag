<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="activeMenu" required="true" type="java.lang.String" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Sistema de Reservas</title>

    <link rel="stylesheet" href="<c:url value="/css/style.css" />">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link rel="stylesheet" type="text/css"
          href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

</head>
<body>

<div class="layout-container">
    <main class="main-content">
        <jsp:doBody/>
        <footer class="page-footer">© 2025 - Sistema de Reservas</footer>
    </main>
</div>

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

<script>
  function showToast(message, type) {
    let backgroundColor;
    if (type === 'success') {
      backgroundColor = "linear-gradient(to right, #00b09b, #96c93d)";
    } else if (type === 'error') {
      backgroundColor = "linear-gradient(to right, #ff5f6d, #ffc371)";
    }

    Toastify({
      text: message,
      duration: 5000,
      close: true,
      gravity: "top",
      position: "right",
      stopOnFocus: true, // Para o tempo quando o mouse está em cima
      style: {
        background: backgroundColor,
      }
    }).showToast();
  }

  // Lógica para ler as mensagens passadas pelo Spring via RedirectAttributes
  <c:if test="${not empty mensagemSucesso}">
  showToast('${mensagemSucesso}', 'success');
  </c:if>

  <c:if test="${not empty erros}">
  <c:forEach var="erro" items="${erros}">
  showToast('${erro}', 'error');
  </c:forEach>
  </c:if>
</script>
</body>
</html>