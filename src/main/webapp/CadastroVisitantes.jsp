<%@page contentType="text/html" pageEncoding="Latin1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=Latin1">
    <title>Cadastro de Visitantes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilo/estilo.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        h1, h2 { color: #333; text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        tr:hover { background-color: #f1f1f1; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
        input[type=text], input[type=email], input[type=number], select { width: 100%; padding: 10px; margin: 5px 0 15px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        input[type=submit], input[type=button], button { background-color: #4CAF50; color: white; padding: 12px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        input[type=submit]:hover, button:hover { background-color: #45a049; }
        input[type=button] { background-color: #f44336; }
        input[type=button]:hover { background-color: #da190b; }
        fieldset { border: 1px solid #ddd; padding: 20px; border-radius: 5px; margin-bottom: 20px; background-color: #fff; }
        legend { font-size: 1.5em; font-weight: bold; color: #333; }
        .mensagem { padding: 15px; margin-bottom: 20px; border-radius: 4px; font-size: 16px; }
        .sucesso { color: #155724; background-color: #d4edda; border-color: #c3e6cb; }
        .erro { color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; }
        nav ul { list-style-type: none; padding: 10px; background-color: #333; text-align: center; }
        nav ul li { display: inline; margin-right: 20px; }
        nav ul li a { color: white; text-decoration: none; font-weight: bold; }
        nav ul li a:hover { color: #ccc; }
    </style>
</head>
<body>
    <%@include file="menu.jsp" %>
    <h1>Gerenciamento de Visitantes</h1>

    <form action="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador" method="GET">
        <input type="hidden" name="codVisitante" value="${not empty codVisitante ? codVisitante : '0'}"/>
        <input type="hidden" name="opcao" value="${not empty opcao ? opcao : 'cadastrar'}"/>

        <fieldset>
            <legend>${not empty codVisitante and codVisitante != '0' ? 'Editar Visitante' : 'Novo Visitante'}</legend>

            <label for="nome">Nome:</label><br>
            <input type="text" name="nome" id="nome" value="${nome}" required/><br>

            <label for="emailVisitante">Email:</label><br>
            <input type="email" name="emailVisitante" id="emailVisitante" value="${emailVisitante}" required/><br>

            <label for="cpf">CPF (somente números):</label><br>
            <input type="text" name="cpf" id="cpf" value="${cpf}" required title="Digite 11 números para o CPF."/><br>

            <input type="submit" value="Salvar"/>
            <a href="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador?opcao=cancelar">
                <input type="button" value="Cancelar"/>
            </a>
        </fieldset>
    </form>

    <c:if test="${not empty mensagem}">
        <p class="mensagem erro">${mensagem}</p>
    </c:if>

    <hr>
    
    <form action="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador" method="get" target="_blank" style="margin-bottom: 20px;">
        <input type="hidden" name="opcao" value="gerarRelatorio">
        <button type="submit">Gerar Relatório em PDF</button>
    </form>

    <h2>Visitantes Cadastrados</h2>
    <table>
        <thead>
            <tr>
                <th>Cód.</th>
                <th>Nome</th>
                <th>Email</th>
                <th>CPF</th>
                <th colspan="2">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="visitante" items="${listaVisitantes}">
                <tr>
                    <td>${visitante.codVisitante}</td>
                    <td>${visitante.nome}</td>
                    <td>${visitante.emailVisitante}</td>
                    <td>${visitante.cpf}</td>
                    <td><a href="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador?opcao=editar&codVisitante=${visitante.codVisitante}">Editar</a></td>
                    <td><a href="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador?opcao=excluir&codVisitante=${visitante.codVisitante}">Excluir</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>