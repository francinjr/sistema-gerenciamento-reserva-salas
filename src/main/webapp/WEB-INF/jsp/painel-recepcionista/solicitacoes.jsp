<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<%--
  Usamos o layout principal. Pode criar um novo item 'solicitacoes' no menu
  do template.tag e passar "solicitacoes" como activeMenu para o destacar.
--%>
<layout:template title="Solicitações de Agendamento" activeMenu="solicitacoes">
  <div class="content-card">
    <div class="card-header">
      <h1>Solicitações de Agendamento Pendentes</h1>
    </div>

    <div class="table-wrapper">
      <table>
        <thead>
        <tr>
          <th>Cliente</th>
          <th>Sala</th>
          <th>Início</th>
          <th>Fim</th>
          <th>Pessoas</th>
          <th style="width: 1%; white-space: nowrap;">Ações</th>
        </tr>
        </thead>
        <tbody>
          <%-- Verifica se a lista de agendamentos está vazia --%>
        <c:if test="${empty agendamentosPage.content}">
          <tr>
            <td colspan="6" style="text-align: center; color: #6c757d;">
              Nenhuma solicitação pendente no momento.
            </td>
          </tr>
        </c:if>

          <%-- Itera sobre a lista de agendamentos para os exibir --%>
        <c:forEach var="agendamento" items="${agendamentosPage.content}">
          <tr>
            <td><c:out value="${agendamento.nomeCliente}"/></td>
            <td><c:out value="${agendamento.nomeSala}"/></td>
              <%-- Formata a data e hora para um formato legível --%>
            <td><c:out value="${agendamento.dataHoraInicio}"/></td>
            <td><c:out value="${agendamento.dataHoraFim}"/></td>
            <td><c:out value="${agendamento.quantidadePessoas}"/></td>
            <td class="actions-cell">

              <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/confirmar' />" method="post" style="display:inline;">
                <button type="submit" class="btn btn-primary">Confirmar</button>
              </form>

              <form action="<c:url value='/painel-recepcionista/solicitacoes/${agendamento.id}/recusar' />" method="post" style="display:inline;">
                <button type="submit" class="btn btn-danger-outline">Recusar</button>
              </form>

            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>

      <%-- A secção do rodapé com a paginação --%>
    <div class="card-footer">
      <div class="items-per-page">
        <span>Itens por página:</span>
        <select name="size">
          <option value="10" ${agendamentosPage.size == 10 ? 'selected' : ''}>10</option>
          <option value="20" ${agendamentosPage.size == 20 ? 'selected' : ''}>20</option>
        </select>
      </div>
      <div class="page-info">
        <span>Página ${agendamentosPage.number + 1} de ${agendamentosPage.totalPages > 0 ? agendamentosPage.totalPages : 1}</span>
      </div>
      <div class="pagination-nav">
        <c:url var="prevUrl" value="/painel-recepcionista/solicitacoes">
          <c:param name="page" value="${agendamentosPage.number - 1}"/>
          <c:param name="size" value="${agendamentosPage.size}"/>
        </c:url>
        <a href="${prevUrl}" class="btn-nav ${agendamentosPage.first ? 'disabled' : ''}"><i class="fa-solid fa-chevron-left"></i></a>

        <c:url var="nextUrl" value="/painel-recepcionista/solicitacoes">
          <c:param name="page" value="${agendamentosPage.number + 1}"/>
          <c:param name="size" value="${agendamentosPage.size}"/>
        </c:url>
        <a href="${nextUrl}" class="btn-nav ${agendamentosPage.last ? 'disabled' : ''}"><i class="fa-solid fa-chevron-right"></i></a>
      </div>
    </div>
  </div>
</layout:template>