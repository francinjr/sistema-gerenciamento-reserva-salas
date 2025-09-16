<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- Importa a biblioteca de formatação --%>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:template title="Gestão de Salas" activeMenu="salas">
    <div class="content-card">
        <div class="card-header">
            <h1>Salas</h1>
            <form id="searchForm" action="<c:url value='/salas/listar' />" method="get"
                  class="header-controls">
                <input type="hidden" name="size" value="${salasPage.size}">
                <div class="search-container">
                    <i class="fa-solid fa-magnifying-glass search-icon"></i>
                    <input type="text" id="searchInput" name="nome"
                           placeholder="Pesquisar por nome..." value="${termoBusca}"
                           onkeyup="debounceSearch()">
                </div>
                <a href="<c:url value="/salas/novo" />" class="btn btn-primary">Nova Sala</a>
            </form>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Preço por Período</th>
                    <th>Descrição</th>
                    <th>Setor</th>
                    <th style="width: 1%; white-space: nowrap;">Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty salasPage.content}">
                    <tr>
                        <td colspan="5" style="text-align: center; color: #6c757d;">Nenhuma sala
                            encontrada.
                        </td>
                    </tr>
                </c:if>
                <c:forEach var="sala" items="${salasPage.content}">
                    <tr>
                        <td><c:out value="${sala.nome}"/></td>
                        <td>
                                <%-- Formata o valor numérico como moeda brasileira --%>
                            <fmt:setLocale value="pt_BR"/>
                            <fmt:formatNumber value="${sala.preco}" type="currency"/>
                        </td>
                        <td><c:out value="${sala.descricao}"/></td>
                        <td><c:out value="${sala.nomeSetor}"/></td>
                        <td class="actions-cell">
                            <a href="<c:url value='/salas/editar/${sala.id}' />"
                               class="btn btn-secondary">Editar</a>
                            <form id="deleteForm-${sala.id}"
                                  action="<c:url value='/salas/excluir/${sala.id}' />" method="post"
                                  style="display:inline;">
                                <button type="button" class="btn btn-danger-outline"
                                        onclick="openDeleteModal('deleteForm-${sala.id}', 'Deseja realmente deletar a sala \'${sala.nome}\'?')">
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
            <form id="pageSizeForm" action="<c:url value='/salas/listar' />" method="get">
                <input type="hidden" name="nome" value="${termoBusca}">
                <div class="items-per-page">
                    <span>Itens por página:</span>
                    <select name="size" onchange="this.form.submit()">
                        <option value="10" ${salasPage.size == 10 ? 'selected' : ''}>10</option>
                        <option value="20" ${salasPage.size == 20 ? 'selected' : ''}>20</option>
                        <option value="30" ${salasPage.size == 30 ? 'selected' : ''}>30</option>
                    </select>
                </div>
            </form>
            <div class="page-info">
                <span>Página ${salasPage.number + 1} de ${salasPage.totalPages > 0 ? salasPage.totalPages : 1}</span>
            </div>
            <div class="pagination-nav">
                <c:url var="prevUrl" value="/salas/listar"><c:param name="page"
                                                                    value="${salasPage.number - 1}"/><c:param
                        name="size" value="${salasPage.size}"/><c:param name="nome"
                                                                        value="${termoBusca}"/></c:url>
                <a href="${prevUrl}" class="${salasPage.first ? 'disabled' : ''}"><i
                        class="fa-solid fa-chevron-left"></i></a>
                <c:url var="nextUrl" value="/salas/listar"><c:param name="page"
                                                                    value="${salasPage.number + 1}"/><c:param
                        name="size" value="${salasPage.size}"/><c:param name="nome"
                                                                        value="${termoBusca}"/></c:url>
                <a href="${nextUrl}" class="${salasPage.last ? 'disabled' : ''}"><i
                        class="fa-solid fa-chevron-right"></i></a>
            </div>
        </div>
    </div>
    <script>
      let debounceTimer;

      function debounceSearch() {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
          document.getElementById('searchForm').submit();
        }, 400);
      }
    </script>
</layout:template>