<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--@elvariable id="salaDto" type="com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.SalvarSalaDto"--%>
<%--@elvariable id="salaId" type="java.lang.Long"--%>
<%--@elvariable id="setores" type="java.util.List<com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor>"--%>

<layout:template title="${empty salaId ? 'Nova Sala' : 'Editar Sala'}" activeMenu="salas">
    <div class="content-card">
        <div class="card-header">
            <h1>${empty salaId ? 'Adicionar Nova Sala' : 'Editar Sala'}</h1>
        </div>

        <div class="card-body">
            <c:url var="postUrl"
                   value="${empty salaId ? '/salas/salvar' : '/salas/atualizar/'.concat(salaId)}"/>

            <form:form modelAttribute="salaDto" action="${postUrl}" method="post"
                       cssClass="form-main">

                <div class="form-group">
                    <form:label path="nome">Nome da Sala</form:label>
                    <form:input path="nome" cssClass="form-control"
                                placeholder="Ex: Sala de Reunião Principal"/>
                    <form:errors path="nome" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="preco">Preço por Período</form:label>
                    <input type="text" id="preco-visual" class="form-control"
                           placeholder="R$ 0,00"/>
                    <form:hidden path="preco" id="preco-hidden"/>
                    <form:errors path="preco" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="descricao">Descrição</form:label>
                    <form:textarea path="descricao" cssClass="form-control" rows="4"
                                   placeholder="Uma breve descrição sobre a sala e seus recursos..."/>
                    <form:errors path="descricao" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <form:label path="setorId">Setor</form:label>
                    <form:select path="setorId" cssClass="form-control">
                        <form:option value="" label="-- Selecione um Setor --"/>
                        <form:options items="${setores}" itemValue="id" itemLabel="nome"/>
                    </form:select>
                    <form:errors path="setorId" cssClass="form-error"/>
                </div>

                <div class="form-actions">
                    <button type="submit"
                            class="btn btn-primary">${empty salaId ? 'Salvar Sala' : 'Atualizar Sala'}</button>
                    <a href="<c:url value='/salas/listar' />" class="btn btn-secondary">Cancelar</a>
                </div>
            </form:form>
        </div>
    </div>

    <script>
      // Garante que o script rode apenas depois que a página estiver totalmente carregada
      document.addEventListener('DOMContentLoaded', function () {

        // Seleciona os dois campos de input (o visível e o oculto)
        const precoVisualEl = document.getElementById('preco-visual');
        const precoHiddenEl = document.getElementById('preco-hidden');

        // Configurações da máscara para moeda brasileira (BRL)
        const currencyMaskOptions = {
          mask: 'R$ num',
          blocks: {
            num: {
              mask: Number,
              scale: 2, // 2 casas decimais
              thousandsSeparator: '.', // Separador de milhar
              padFractionalZeros: true, // Adiciona zeros no final (ex: 12,3 -> 12,30)
              radix: ',', // Separador decimal
              mapToRadix: ['.'] // Permite digitar '.' e ele vira ','
            }
          }
        };

        const mask = IMask(precoVisualEl, currencyMaskOptions);
        if (precoHiddenEl.value) {
          mask.value = precoHiddenEl.value;
        }

        mask.on('accept', function () {
          precoHiddenEl.value = mask.unmaskedValue;
        }, false);
      });
    </script>
</layout:template>