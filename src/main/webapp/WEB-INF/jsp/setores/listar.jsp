<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:template title="Gestão de Setores" activeMenu="setores">
    <div class="content-card">
        <div class="card-header">
            <h1>Setores</h1>
            <div class="header-controls">
                <form id="searchForm" action="<c:url value='/setores/listar' />" method="get" class="header-controls">
                    <input type="hidden" name="size" value="${setoresPage.size}">
                    <div class="search-container">
                        <i class="fa-solid fa-magnifying-glass search-icon"></i>
                        <input type="text" id="searchInput" name="nome"
                               placeholder="Pesquisar por nome..."
                               value="${termoBusca}" onkeyup="debounceSearch()">
                    </div>
                    <a href="<c:url value="/setores/novo" />" class="btn btn-primary">Novo Setor</a>
                </form>
            </div>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th style="width: 1%; white-space: nowrap;">Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty setoresPage.content}">
                    <tr>
                        <td colspan="3" style="text-align: center; color: #6c757d;">Nenhum setor
                            encontrado.
                        </td>
                    </tr>
                </c:if>

                <c:forEach var="setor" items="${setoresPage.content}">
                    <tr>
                        <td><c:out value="${setor.nome}"/></td>
                        <td><c:out value="${setor.descricao}"/></td>
                        <td class="actions-cell">

                                <%-- Ação para Editar --%>
                            <a href="<c:url value='/setores/editar/${setor.id}' />" class="btn btn-secondary">Editar</a>

                                <%-- Formulário para a ação de Excluir --%>
                            <form id="deleteForm-${setor.id}"
                                  action="<c:url value='/setores/excluir/${setor.id}' />"
                                  method="post" style="display:inline;">
                                <button type="button" class="btn btn-danger-outline"
                                        onclick="openDeleteModal('deleteForm-${setor.id}', 'Deseja realmente deletar o setor \'${setor.nome}\'?')">
                                    Excluir
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card-footer">
            <form id="pageSizeForm" action="<c:url value='/setores/listar' />" method="get">
                <input type="hidden" name="nome" value="${termoBusca}">
                <div class="items-per-page">
                    <span>Itens por página:</span>
                    <select name="size" onchange="this.form.submit()">
                        <option value="10" ${setoresPage.size == 10 ? 'selected' : ''}>10</option>
                        <option value="20" ${setoresPage.size == 20 ? 'selected' : ''}>20</option>
                        <option value="30" ${setoresPage.size == 30 ? 'selected' : ''}>30</option>
                        <option value="40" ${setoresPage.size == 40 ? 'selected' : ''}>40</option>
                        <option value="50" ${setoresPage.size == 50 ? 'selected' : ''}>50</option>
                    </select>
                </div>
            </form>

            <div class="page-info">
                <span>Página ${setoresPage.number + 1} de ${setoresPage.totalPages > 0 ? setoresPage.totalPages : 1}</span>
            </div>

            <div class="pagination-nav">
                <c:url var="prevUrl" value="/setores/listar">
                    <c:param name="page" value="${setoresPage.number - 1}"/>
                    <c:param name="size" value="${setoresPage.size}"/>
                    <c:param name="nome" value="${termoBusca}"/>
                </c:url>
                <a href="${prevUrl}" class="btn-nav ${setoresPage.first ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>

                <c:url var="nextUrl" value="/setores/listar">
                    <c:param name="page" value="${setoresPage.number + 1}"/>
                    <c:param name="size" value="${setoresPage.size}"/>
                    <c:param name="nome" value="${termoBusca}"/>
                </c:url>
                <a href="${nextUrl}" class="btn-nav ${setoresPage.last ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
            </div>
        </div>
    </div>

    <%-- SCRIPT PARA BUSCA AUTOMÁTICA --%>
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