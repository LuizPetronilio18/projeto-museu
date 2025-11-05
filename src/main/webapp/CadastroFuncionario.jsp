<%@page contentType="text/html" pageEncoding="Latin1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=Latin1">
    <title>Cadastro de Funcionários</title>
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
        input[type=text], input[type=number], select { width: 100%; padding: 10px; margin: 5px 0 15px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
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
    
    <h1>Gerenciamento de Funcionários</h1>

    <form action="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador" method="GET">
        <input type="hidden" name="codFuncionario" value="${not empty codFuncionario ? codFuncionario : '0'}"/>
        <input type="hidden" name="opcao" value="${not empty opcao ? opcao : 'cadastrar'}"/>

        <fieldset>
            <legend>${not empty codFuncionario and codFuncionario != '0' ? 'Editar Funcionário' : 'Novo Funcionário'}</legend>

            <label for="nome">Nome:</label><br>
            <input type="text" name="nome" id="nome" value="${nome}" required/><br>

            <label for="cpf">CPF:</label><br>
            <input type="text" name="cpf" id="cpf" value="${cpf}" required/><br>

            <label for="cargo">Cargo:</label><br>
            <select name="cargo_codCargo" id="cargo" required>
                <option value="">Selecione um Cargo</option>
                <c:forEach var="cargo" items="${listaCargo}">
                    <option value="${cargo.codCargo}" ${cargo_codCargo == cargo.codCargo ? 'selected' : ''}>
                        ${cargo.nomeCargo}
                    </option>
                </c:forEach>
            </select><br>

            <input type="submit" value="Salvar"/>
            <a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=cancelar">
                <input type="button" value="Cancelar"/>
            </a>
        </fieldset>
    </form>

    <hr>
    
    <form action="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador" method="get" target="_blank" style="margin-bottom: 20px;">
        <input type="hidden" name="opcao" value="gerarRelatorio">
        <button type="submit">Gerar Relatório em PDF</button>
    </form>

    <h2>Funcionários Cadastrados</h2>
    <table>
        <thead>
            <tr>
                <th>Cód.</th>
                <th>Nome</th>
                <th>CPF</th>
                <th>Cargo</th>
                <th colspan="2">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="funcionario" items="${listaFuncionario}">
                <tr>
                    <td>${funcionario.codFuncionario}</td>
                    <td>${funcionario.nome}</td>
                    <td>${funcionario.cpf}</td>
                    <td>${funcionario.cargo_codCargo.nomeCargo}</td>
                    <td><a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=editar&codFuncionario=${funcionario.codFuncionario}&nome=${funcionario.nome}&cpf=${funcionario.cpf}&cargo_codCargo=${funcionario.cargo_codCargo.codCargo}">Editar</a></td>
                    <td><a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=excluir&codFuncionario=${funcionario.codFuncionario}&nome=${funcionario.nome}&cpf=${funcionario.cpf}&cargo_codCargo=${funcionario.cargo_codCargo.codCargo}">Excluir</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>