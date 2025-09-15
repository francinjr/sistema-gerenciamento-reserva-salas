<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<%--
  O layout é usado passando o título da página e qual menu deve ficar ativo.
--%>
<layout:template title="Gestão de Setores" activeMenu="setores">

    <div class="content-card">
        <div class="card-header">
            <h1>Setores</h1>
            <div class="header-controls">
                <div class="search-container">
                    <i class="fa-solid fa-magnifying-glass search-icon"></i>
                    <input type="text" placeholder="Pesquisar">
                </div>
                <a href="/setores/novo" class="btn btn-primary">Novo Setor</a>
            </div>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="setor" items="${setores}">
                    <tr>
                        <td><c:out value="${setor.nome}"/></td>
                        <td><c:out value="${setor.descricao}"/></td>
                        <td class="actions-cell">
                            <a href="/setores/editar/${setor.id}" class="btn btn-secondary">Editar</a>
                            <a href="#" class="btn btn-danger-outline">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card-footer">
            <div class="items-per-page">
                <span>Itens por página:</span>
                <select name="size">
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="30">30</option>
                    <option value="40">40</option>
                    <option value="50">50</option>
                </select>
            </div>
            <div class="pagination">
                <span>Página 1 de 1</span>
                <div class="pagination-nav">
                    <button disabled><i class="fa-solid fa-chevron-left"></i></button>
                    <button><i class="fa-solid fa-chevron-right"></i></button>
                </div>
            </div>
        </div>
    </div>

</layout:template>