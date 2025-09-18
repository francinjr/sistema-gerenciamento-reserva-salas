<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<%--
  Usamos o layout principal da aplicação, pois esta é uma tela para um usuário logado.
  O menu "Início" será marcado como ativo.
--%>
<layout:template title="Dashboard" activeMenu="inicio">
    <div class="dashboard-container">
        <h1>Salas Disponíveis</h1>
        <p class="subtitle">Navegue pelos setores e solicite o agendamento da sala ideal para você.</p>

            <%-- Formulário para a busca por nome de setor, com envio automático via JavaScript --%>
        <form id="searchForm" action="<c:url value='/' />" method="get">
            <div class="search-container dashboard-search">
                <i class="fa-solid fa-magnifying-glass search-icon"></i>
                <input type="text" id="searchInput" name="nomeSetor"
                       placeholder="Pesquisar por nome do setor..."
                       value="${termoBuscaSetor}" onkeyup="debounceSearch()">
            </div>
        </form>

            <%-- Loop principal que cria um card para cada setor retornado pelo serviço --%>
        <c:forEach var="setor" items="${setores}">
            <div class="setor-card">
                <div class="setor-card-header">
                    <h2><i class="fa-solid fa-sitemap"></i> <c:out value="${setor.nome}"/></h2>
                    <p><c:out value="${setor.descricao}"/></p>
                </div>

                <div class="salas-grid">
                        <%-- Loop aninhado que exibe cada sala dentro do card do setor --%>
                    <c:forEach var="sala" items="${setor.salas}">
                        <div class="sala-item">
                            <div class="sala-item-info">
                                <h3><c:out value="${sala.nome}"/></h3>
                                <span>Capacidade: ${sala.capacidadeMaxima} pessoas</span>
                            </div>

                                <%--
                                  Lógica condicional para exibir a ação correta para o cliente:
                                  - Se o agendamento foi CONFIRMADO, mostra a mensagem verde.
                                  - Se foi SOLICITADO, mostra o botão para ver/cancelar a solicitação.
                                  - Senão, mostra o botão para solicitar um novo agendamento.
                                --%>
                            <c:choose>
                                <c:when test="${sala.statusAgendamento == 'CONFIRMADO'}">
                                    <div class="agendamento-status status-confirmado">
                                        <i class="fa-solid fa-circle-check"></i>
                                        <span>Seu agendamento foi confirmado</span>
                                    </div>
                                </c:when>
                                <c:when test="${sala.statusAgendamento == 'SOLICITADO'}">
                                    <a href="<c:url value='/agendamentos/solicitacao/${sala.agendamentoId}'/>" class="btn btn-warning-outline">
                                        Ver Solicitação
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/agendamentos/solicitar/${sala.id}'/>" class="btn btn-primary">
                                        Solicitar Agendamento
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                    <c:if test="${empty setor.salas}">
                        <p class="no-data-inline">Nenhuma sala cadastrada neste setor.</p>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

    <%-- SCRIPT PARA BUSCA AUTOMÁTICA (AS-YOU-TYPE) --%>
    <script>
      let debounceTimer;

      function debounceSearch() {
        // Cancela o timer anterior para evitar múltiplas submissões
        clearTimeout(debounceTimer);

        // Cria um novo timer
        debounceTimer = setTimeout(() => {
          // Após 400ms sem digitação, submete o formulário de busca
          document.getElementById('searchForm').submit();
        }, 400); // 400 milissegundos de espera
      }
    </script>
</layout:template>