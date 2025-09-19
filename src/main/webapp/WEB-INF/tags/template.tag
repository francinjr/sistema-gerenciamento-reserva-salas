<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="activeMenu" required="true" type="java.lang.String" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Sistema de Reservas</title>
    <link rel="stylesheet" href="<c:url value="/css/style.css" />">
    <link rel="stylesheet" href="<c:url value="/css/login.css" />">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
</head>
<body>
<div class="layout-container">
    <aside class="sidebar">
        <div class="sidebar-header"><span class="logo-text">Sistema de Reservas</span></div>
        <nav class="sidebar-nav">
            <ul>
                <%-- Link "Início" unificado para todos os usuários logados --%>
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <a href="<c:url value="/inicio" />" class="${activeMenu == 'inicio' ? 'active' : ''}">
                            <i class="fa-solid fa-house-chimney"></i> Início
                        </a>
                    </li>
                </sec:authorize>

                <%-- Links visíveis apenas para o Cliente (puro) --%>
                <sec:authorize access="hasRole('ROLE_CLIENTE') and !hasAnyRole('ROLE_RECEPCIONISTA', 'ROLE_ADMINISTRADOR')">
                    <li>
                        <a href="<c:url value="/" />" class="${activeMenu == 'dashboard_cliente' ? 'active' : ''}">
                            <i class="fa-solid fa-border-all"></i> Salas Disponíveis
                        </a>
                    </li>
                </sec:authorize>

                <%-- Links visíveis apenas para a Recepcionista --%>
                <sec:authorize access="hasRole('ROLE_RECEPCIONISTA') and !hasRole('ROLE_ADMINISTRADOR')">
                    <li><a href="<c:url value="/painel-recepcionista/dashboard" />" class="${activeMenu == 'painel' ? 'active' : ''}"><i class="fa-solid fa-clipboard-list"></i> Painel de Agendamentos</a></li>
                    <li><a href="<c:url value="/painel-recepcionista/agendar-sala" />" class="${activeMenu == 'agendar_sala' ? 'active' : ''}"><i class="fa-solid fa-calendar-plus"></i> Agendar Sala</a></li>
                </sec:authorize>

                <%-- Link de Relatórios visível para todos os usuários logados --%>
                <sec:authorize access="isAuthenticated()">
                    <li><a href="<c:url value="/relatorios/agendamentos" />" class="${activeMenu == 'relatorios' ? 'active' : ''}"><i class="fa-solid fa-chart-line"></i> Relatórios</a></li>
                </sec:authorize>

                <%-- Links visíveis apenas para o Administrador --%>
                <sec:authorize access="hasRole('ROLE_ADMINISTRADOR')">
                    <li><a href="<c:url value="/setores/listar" />" class="${activeMenu == 'setores' ? 'active' : ''}"><i class="fa-solid fa-sitemap"></i> Gerir Setores</a></li>
                    <li><a href="<c:url value="/salas/listar" />" class="${activeMenu == 'salas' ? 'active' : ''}"><i class="fa-solid fa-door-closed"></i> Gerir Salas</a></li>
                    <li><a href="<c:url value="/recepcionistas/listar" />" class="${activeMenu == 'recepcionistas' ? 'active' : ''}"><i class="fa-solid fa-concierge-bell"></i> Gerir Recepcionistas</a></li>
                </sec:authorize>
            </ul>
        </nav>
        <div class="sidebar-footer">
            <form id="logoutForm" action="<c:url value="/logout"/>" method="post">
                <sec:csrfInput/>
                <a href="#" onclick="document.getElementById('logoutForm').submit(); return false;"><i class="fa-solid fa-arrow-right-from-bracket"></i> Sair</a>
            </form>
        </div>
    </aside>
    <main class="main-content">
        <jsp:doBody/>
        <footer class="page-footer">© 2025 - Sistema de Reservas</footer>
    </main>
</div>
<div id="deleteModal" class="modal-overlay">
    <div class="modal">
        <div class="modal-header"><h3>Confirmar Ação</h3>
            <button class="close-modal-btn">&times;</button>
        </div>
        <div class="modal-body"><p id="deleteModalMessage">Deseja realmente executar esta ação?</p>
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary close-modal-btn">Não</button>
            <button id="confirmDeleteBtn" class="btn btn-danger">Sim</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script src="https://unpkg.com/imask"></script>
<script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script src="https://unpkg.com/imask"></script>
<script>
  function showToast(message, type) {
    let backgroundColor = (type === 'success')
        ? "linear-gradient(to right, #00b09b, #96c93d)"
        : "linear-gradient(to right, #e53935, #b71c1c)";
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

  const deleteModal = document.getElementById('deleteModal');
  if (deleteModal) {
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
  }
</script>
</body>
</html>