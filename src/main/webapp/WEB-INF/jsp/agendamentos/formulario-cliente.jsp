<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<layout:template title="Solicitar Agendamento" activeMenu="inicio">
  <div class="content-card">
    <div class="card-header"><h1>Solicitar Agendamento: ${sala.nome}</h1></div>
    <div class="card-body">
      <div class="sala-info">
        <p><strong>Setor:</strong> ${sala.setor.nome}</p>
        <p><strong>Capacidade:</strong> ${sala.capacidadeMaxima} pessoas</p>
        <p><strong>Preço por Hora:</strong> <fmt:formatNumber value="${sala.preco.valor}" type="currency"/></p>
      </div>
      <form:form modelAttribute="agendamentoDto" action="/agendamentos/solicitar" method="post" cssClass="form-main">
        <form:hidden path="salaId" />
        <form:errors path="*" cssClass="form-error-global" element="div" />
        <div class="form-group">
          <form:label path="dataHoraInicio">Início do Agendamento</form:label>
          <form:input type="datetime-local" path="dataHoraInicio" cssClass="form-control" id="inicio"/>
          <form:errors path="dataHoraInicio" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <form:label path="dataHoraFim">Fim do Agendamento</form:label>
          <form:input type="datetime-local" path="dataHoraFim" cssClass="form-control" id="fim"/>
          <form:errors path="dataHoraFim" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <form:label path="quantidadePessoas">Quantidade de Pessoas</form:label>
          <form:input type="number" path="quantidadePessoas" cssClass="form-control"/>
          <form:errors path="quantidadePessoas" cssClass="form-error"/>
        </div>
        <div class="preco-calculado">
          <div>Valor Total Estimado: <strong id="valor-total">R$ 0,00</strong></div>
          <div>Valor do Sinal (50%): <strong id="valor-sinal">R$ 0,00</strong></div>
          <div>Valor Restante (na finalização): <strong id="valor-finalizacao">R$ 0,00</strong></div>
        </div>
        <div class="form-actions">
          <a href="<c:url value='/' />" class="btn btn-secondary">Cancelar</a>
          <button type="submit" class="btn btn-primary">Enviar Solicitação</button>
        </div>
      </form:form>
    </div>
  </div>
  <script>
    const inicioEl = document.getElementById('inicio');
    const fimEl = document.getElementById('fim');
    const valorTotalEl = document.getElementById('valor-total');
    const valorSinalEl = document.getElementById('valor-sinal');
    const valorFinalizacaoEl = document.getElementById('valor-finalizacao');
    const precoPorHora = parseFloat('${sala.preco.valor}');

    function formatarMoeda(valor) { return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }); }
    function calcularPreco() {
      const inicio = new Date(inicioEl.value);
      const fim = new Date(fimEl.value);
      if (inicioEl.value && fimEl.value && fim > inicio) {
        const diffMs = fim - inicio;
        const horas = (diffMs / 60000) / 60;
        const valorTotal = horas * precoPorHora;
        const valorSinal = valorTotal * 0.5;
        const valorFinalizacao = valorTotal - valorSinal;
        valorTotalEl.textContent = formatarMoeda(valorTotal);
        valorSinalEl.textContent = formatarMoeda(valorSinal);
        valorFinalizacaoEl.textContent = formatarMoeda(valorFinalizacao);
      } else {
        valorTotalEl.textContent = formatarMoeda(0);
        valorSinalEl.textContent = formatarMoeda(0);
        valorFinalizacaoEl.textContent = formatarMoeda(0);
      }
    }
    inicioEl.addEventListener('change', calcularPreco);
    fimEl.addEventListener('change', calcularPreco);
  </script>
</layout:template>