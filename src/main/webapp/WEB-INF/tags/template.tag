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
    <aside class="sidebar">
        <div class="sidebar-header"><span class="logo-text">ReservasApp</span></div>
        <nav class="sidebar-nav">
            <ul>
                <li><a href="#" class="${activeMenu == 'inicio' ? 'active' : ''}"><i
                        class="fa-solid fa-house-chimney"></i> Início</a></li>
                <li><a href="#" class="${activeMenu == 'salas' ? 'active' : ''}"><i
                        class="fa-solid fa-door-closed"></i> Salas</a></li>
                <%-- ✅ CORRIGIDO: Link do menu aponta para a URL correta --%>
                <li><a href="<c:url value="/setores/listar" />"
                       class="${activeMenu == 'setores' ? 'active' : ''}"><i
                        class="fa-solid fa-sitemap"></i> Setores</a></li>
                <li><a href="#" class="${activeMenu == 'recepcionistas' ? 'active' : ''}"><i
                        class="fa-solid fa-concierge-bell"></i> Recepcionistas</a></li>
            </ul>
        </nav>
        <div class="sidebar-footer"><a href="#"><i class="fa-solid fa-arrow-right-from-bracket"></i>
            Sair</a></div>
    </aside>
    <main class="main-content">
        <jsp:doBody/>
        <footer class="page-footer">© 2025 - Sistema de Reservas</footer>
    </main>
</div>
<div id="deleteModal" class="modal-overlay">
    <div class="modal">
        <div class="modal-header"><h3>Confirmar Exclusão</h3>
            <button class="close-modal-btn">&times;</button>
        </div>
        <div class="modal-body"><p id="deleteModalMessage">Deseja realmente deletar este item?</p>
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary close-modal-btn">Não</button>
            <button id="confirmDeleteBtn" class="btn btn-danger">Sim</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script>
  // Lógica para Toasts de feedback
  function showToast(message, type) {
    let backgroundColor = (type === 'success')
        ? "linear-gradient(to right, #00b09b, #96c93d)"
        : "linear-gradient(to right, #ff5f6d, #ffc371)";

    Toastify({
      text: message, duration: 5000, close: true, gravity: "top",
      position: "right", stopOnFocus: true, style: {background: backgroundColor}
    }).showToast();
  }

  <c:if test="${not empty mensagemSucesso}">showToast('${mensagemSucesso}', 'success');
  </c:if>
  <c:if test="${not empty erros}"><c:forEach var="erro" items="${erros}">showToast('${erro}',
      'error');
  </c:forEach></c:if>

  // Lógica para controlar o Modal de Deleção
  const deleteModal = document.getElementById('deleteModal');
  const deleteModalMessage = document.getElementById('deleteModalMessage');
  const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
  const closeModalBtns = document.querySelectorAll('.close-modal-btn');
  let formToSubmit = null;

  function openDeleteModal(formId, message) {
    formToSubmit = document.getElementById(formId);
    deleteModalMessage.textContent = message;
    deleteModal.classList.add('show');
  }

  function closeDeleteModal() {
    deleteModal.classList.remove('show');
    formToSubmit = null;
  }

  confirmDeleteBtn.addEventListener('click', () => {
    if (formToSubmit) {
      formToSubmit.submit();
    }
  });

  closeModalBtns.forEach(btn => btn.addEventListener('click', closeDeleteModal));
  deleteModal.addEventListener('click', (event) => {
    if (event.target === deleteModal) {
      closeDeleteModal();
    }
  });
</script>
</body>
</html>
