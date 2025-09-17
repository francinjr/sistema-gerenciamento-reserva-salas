<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:template title="Gestão de Recepcionistas" activeMenu="recepcionistas">
    <div class="content-card">
        <div class="card-header">
            <h1>Recepcionistas</h1>
            <form id="searchForm" action="<c:url value='/recepcionistas/listar' />" method="get" class="header-controls">
                <input type="hidden" name="size" value="${recepcionistasPage.size}">
                <div class="search-container">
                    <i class="fa-solid fa-magnifying-glass search-icon"></i>
                    <input type="text" id="searchInput" name="nome" placeholder="Pesquisar por nome..." value="${termoBusca}" onkeyup="debounceSearch()">
                </div>
                <a href="<c:url value="/recepcionistas/novo" />" class="btn btn-primary">Novo Recepcionista</a>
            </form>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Setor</th>
                    <th style="width: 1%; white-space: nowrap;">Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="recepcionista" items="${recepcionistasPage.content}">
                    <tr>
                        <td><c:out value="${recepcionista.pessoaFisica.nome}"/></td>
                        <td><c:out value="${recepcionista.usuario.email}"/></td>
                        <td><c:out value="${recepcionista.pessoaFisica.telefone}"/></td>
                        <td><c:out value="${recepcionista.setor.nome}"/></td>
                        <td class="actions-cell">
                            <a href="<c:url value='/recepcionistas/editar/${recepcionista.id}' />" class="btn btn-secondary">Editar</a>
                            <form id="deleteForm-${recepcionista.id}" action="<c:url value='/recepcionistas/excluir/${recepcionista.id}' />" method="post" style="display:inline;">
                                <button type="button" class="btn btn-danger-outline" onclick="openDeleteModal('deleteForm-${recepcionista.id}', 'Deseja realmente deletar o recepcionista \'${recepcionista.pessoaFisica.nome}\'?')">
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
            <form id="pageSizeForm" action="<c:url value='/recepcionistas/listar' />" method="get">
                <input type="hidden" name="nome" value="${termoBusca}">
                <div class="items-per-page">
                    <span>Itens por página:</span>
                    <select name="size" onchange="this.form.submit()">
                        <option value="10" ${recepcionistasPage.size == 10 ? 'selected' : ''}>10</option>
                        <option value="20" ${recepcionistasPage.size == 20 ? 'selected' : ''}>20</option>
                    </select>
                </div>
            </form>
            <div class="page-info"><span>Página ${recepcionistasPage.number + 1} de ${recepcionistasPage.totalPages > 0 ? recepcionistasPage.totalPages : 1}</span></div>
            <div class="pagination-nav">
                <c:url var="prevUrl" value="/recepcionistas/listar"><c:param name="page" value="${recepcionistasPage.number - 1}"/><c:param name="size" value="${recepcionistasPage.size}"/><c:param name="nome" value="${termoBusca}"/></c:url>
                <a href="${prevUrl}" class="${recepcionistasPage.first ? 'disabled' : ''}"><i class="fa-solid fa-chevron-left"></i></a>
                <c:url var="nextUrl" value="/recepcionistas/listar"><c:param name="page" value="${recepcionistasPage.number + 1}"/><c:param name="size" value="${recepcionistasPage.size}"/><c:param name="nome" value="${termoBusca}"/></c:url>
                <a href="${nextUrl}" class="${recepcionistasPage.last ? 'disabled' : ''}"><i class="fa-solid fa-chevron-right"></i></a>
            </div>
        </div>
    </div>
    <script>
      let debounceTimer;
      function debounceSearch() { clearTimeout(debounceTimer); debounceTimer = setTimeout(() => { document.getElementById('searchForm').submit(); }, 400); }
    </script>
</layout:template>