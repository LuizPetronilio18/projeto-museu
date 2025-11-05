<%@page contentType="text/html" pageEncoding="Latin1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=Latin1">
    <title>Ponto de Venda de Ingressos</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; background-color: #f4f7fc; }
        .container { padding: 20px; max-width: 1200px; margin: auto; }
        nav { background-color: #2c3e50; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        nav ul { list-style-type: none; margin: 0; padding: 0; }
        nav ul li { float: left; }
        nav ul li a, .dropbtn { display: inline-block; color: white; text-align: center; padding: 14px 16px; text-decoration: none; }
        nav ul li a:hover, .dropdown:hover .dropbtn { background-color: #34495e; }
        li.dropdown { display: inline-block; }
        .dropdown-content { display: none; position: absolute; background-color: #f9f9f9; min-width: 200px; box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); z-index: 1000; }
        .dropdown-content a { color: black; padding: 12px 16px; text-decoration: none; display: block; text-align: left; }
        .dropdown-content a:hover { background-color: #ddd; }
        .dropdown:hover .dropdown-content { display: block; }
        
        .grid-container { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card h3 { margin-top: 0; color: #3498db; border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; }
        
        select, input[type=date], input[type=number] { width: 100%; padding: 8px; margin-bottom: 10px; border-radius: 4px; border: 1px solid #ccc; }
        .btn, button { padding: 10px 15px; border: none; border-radius: 5px; cursor: pointer; color: white; font-size: 16px; text-decoration: none; display: inline-block; }
        .btn-primary, button { background-color: #3498db; }
        .btn-success { background-color: #2ecc71; }
        .btn-danger { background-color: #e74c3c; }
        
        #tabela-carrinho { width: 100%; border-collapse: collapse; }
        #tabela-carrinho th, #tabela-carrinho td { padding: 8px; border-bottom: 1px solid #ddd; text-align: left; }
        #total-carrinho { text-align: right; font-size: 1.2em; font-weight: bold; margin-top: 10px; }
        .mensagem { padding: 15px; margin-bottom: 20px; border-radius: 4px; font-size: 16px; text-align: center; }
        .sucesso { color: #155724; background-color: #d4edda; border-color: #c3e6cb; }
        .erro { color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; }
    </style>
</head>
<body>
    <%@include file="menu.jsp" %>
    
    <div class="container">
        
        <form action="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador" method="get" target="_blank" style="margin-bottom: 20px;">
            <input type="hidden" name="opcao" value="gerarRelatorio">
            <button type="submit">Gerar Relatório de Vendas</button>
        </form>

        <c:if test="${not empty mensagem}">
            <p class="mensagem sucesso">${mensagem}</p>
        </c:if>

        <c:if test="${empty sessionScope.visitaAtiva}">
            <div class="card">
                <h3>Iniciar Nova Venda</h3>
                <form action="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador" method="POST">
                    <input type="hidden" name="opcao" value="iniciarVenda">
                    <label for="visitante">Selecione o Visitante:</label>
                    <select id="visitante" name="codVisitante" required>
                        <option value="">-- Selecione --</option>
                        <c:forEach var="v" items="${listaVisitantes}">
                            <option value="${v.codVisitante}">${v.nome}</option>
                        </c:forEach>
                    </select>
                    <label for="dataVisita">Data da Visita:</label>
                    <input type="date" id="dataVisita" name="dataVisita" required>
                    <button type="submit" class="btn btn-primary">Iniciar Venda</button>
                </form>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.visitaAtiva}">
            <div class="card" style="margin-bottom: 20px;">
                <h3>Venda em Andamento</h3>
                <p><strong>Visitante:</strong> ${sessionScope.visitaAtiva.visitante.nome}</p>
                <p><strong>Data:</strong> <fmt:formatDate value="${sessionScope.visitaAtiva.data.time}" pattern="dd/MM/yyyy"/></p>
                <a href="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador?opcao=cancelarVenda" class="btn btn-danger">Cancelar Venda</a>
            </div>

            <div class="grid-container">
                <div class="card">
                    <h3>Adicionar Ingressos</h3>
                    <form action="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador" method="POST">
                        <input type="hidden" name="opcao" value="adicionarCarrinho">
                        <label for="museu-select">Museu:</label>
                        <select id="museu-select" name="museuId" required>
                             <c:forEach var="m" items="${listaMuseus}">
                                 <option value="${m.codMuseu}">${m.nome}</option>
                             </c:forEach>
                        </select>
                        <label for="tipo-ingresso">Tipo de Ingresso:</label>
                        <select id="tipo-ingresso" name="tipo" onchange="document.getElementById('preco-input').value = this.options[this.selectedIndex].dataset.preco" required>
                            <option value="Inteira" data-preco="50.00">Inteira (R$ 50,00)</option>
                            <option value="Meia" data-preco="25.00">Meia (R$ 25,00)</option>
                        </select>
                        <input type="hidden" id="preco-input" name="preco" value="50.00">
                        <label for="quantidade">Quantidade:</label>
                        <input type="number" id="quantidade" name="quantidade" value="1" min="1">
                        <button type="submit" class="btn btn-primary">Adicionar ao Carrinho</button>
                    </form>
                </div>

                <div class="card">
                    <h3><i class="fas fa-shopping-cart"></i> Carrinho</h3>
                    <table id="tabela-carrinho">
                        <thead>
                            <tr>
                                <th>Museu</th>
                                <th>Tipo</th>
                                <th>Preço</th>
                                <th>Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach var="item" items="${sessionScope.carrinho}" varStatus="status">
                                <tr>
                                    <td>${item.museu.nome}</td>
                                    <td>${item.tipo}</td>
                                    <td><fmt:formatNumber value="${item.preco}" type="currency" currencySymbol="R$ "/></td>
                                    <td><a href="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador?opcao=removerCarrinho&index=${status.index}" class="btn btn-danger" style="padding: 5px 10px;">X</a></td>
                                </tr>
                                <c:set var="total" value="${total + item.preco}"/>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div id="total-carrinho">Total: <fmt:formatNumber value="${total}" type="currency" currencySymbol="R$ "/></div>
                    <a href="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador?opcao=finalizarVenda" class="btn btn-success" style="width:100%; margin-top: 10px; text-align:center;">Finalizar Compra</a>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>