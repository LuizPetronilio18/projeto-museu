<%@page contentType="text/html" pageEncoding="Latin1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=Latin1">
        <title>Cadastro de Artistas</title>
        <%-- Seus estilos CSS permanecem os mesmos --%>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilo/estilo.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                background-color: #f4f4f4;
            }
            h1, h2 {
                color: #333;
                text-align: center;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            th, td {
                border: 1px solid #ddd;
                padding: 12px;
                text-align: left;
            }
            th {
                background-color: #4CAF50;
                color: white;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            a {
                color: #007bff;
                text-decoration: none;
            }
            a:hover {
                text-decoration: underline;
            }
            input[type=text], input[type=number], select {
                width: 100%;
                padding: 10px;
                margin: 5px 0 15px 0;
                display: inline-block;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            input[type=submit], input[type=button], button {
                background-color: #4CAF50;
                color: white;
                padding: 12px 20px;
                margin: 8px 0;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
            }
            input[type=submit]:hover, button:hover {
                background-color: #45a049;
            }
            input[type=button] {
                background-color: #f44336;
            }
            input[type=button]:hover {
                background-color: #da190b;
            }
            fieldset {
                border: 1px solid #ddd;
                padding: 20px;
                border-radius: 5px;
                margin-bottom: 20px;
                background-color: #fff;
            }
            legend {
                font-size: 1.5em;
                font-weight: bold;
                color: #333;
            }
            .mensagem {
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 4px;
                font-size: 16px;
            }
            .sucesso {
                color: #155724;
                background-color: #d4edda;
                border-color: #c3e6cb;
            }
            .erro {
                color: #721c24;
                background-color: #f8d7da;
                border-color: #f5c6cb;
            }
            nav ul {
                list-style-type: none;
                padding: 10px;
                background-color: #333;
                text-align: center;
            }
            nav ul li {
                display: inline;
                margin-right: 20px;
            }
            nav ul li a {
                color: white;
                text-decoration: none;
                font-weight: bold;
            }
            nav ul li a:hover {
                color: #ccc;
            }
        </style>
    </head>
    <body>
        <%@include file="menu.jsp" %>

        <h1>Gerenciamento de Artistas</h1>

        <%-- CORREÇÃO PRINCIPAL: Alterado method="GET" para method="POST" --%>
        <form action="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador" method="POST">
            
            <%-- Campos ocultos para controlar a ação (cadastrar, editar, excluir) --%>
            <input type="hidden" name="codArtista" value="${not empty artista ? artista.codArtista : '0'}"/>
            <input type="hidden" name="opcao" value="${not empty opcao ? opcao : 'cadastrar'}"/>

            <fieldset>
                <%-- O título agora usa o objeto 'artista' para decidir o que mostrar --%>
                <legend>${opcao == 'confirmarEditar' ? 'Editar Artista' : (opcao == 'confirmarExcluir' ? 'Excluir Artista' : 'Novo Artista')}</legend>

                <label for="nomeArtista">Nome do Artista:</label><br>
                <%-- O valor dos campos agora vem do objeto 'artista' e eles podem ser desabilitados --%>
                <input type="text" name="nomeArtista" id="nomeArtista" value="${not empty artista ? artista.nomeArtista : ''}" ${readOnly ? 'readonly' : ''} required/><br>

                <label for="nacionalidadae">Nacionalidade:</label><br>
                <input type="text" name="nacionalidadae" id="nacionalidadae" value="${not empty artista ? artista.nacionalidadae : ''}" ${readOnly ? 'readonly' : ''} required/><br>

                <input type="submit" value="Salvar"/>
                <a href="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador?opcao=cancelar">
                    <input type="button" value="Cancelar"/>
                </a>
            </fieldset>
        </form>

        <%-- Exibição de mensagens de sucesso ou erro --%>
        <c:if test="${not empty erro || not empty mensagem}">
            <p class="mensagem ${not empty erro ? 'erro' : 'sucesso'}">${not empty erro ? erro : mensagem}</p>
        </c:if>

        <hr>
        
        <%-- Formulário para gerar relatório em PDF --%>
        <form action="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador" method="GET" target="_blank">
            <input type="hidden" name="opcao" value="gerarRelatorio">
            <button type="submit">Gerar Relatório em PDF</button>
        </form>
        
        <h2>Artistas Cadastrados</h2>
        <table>
            <thead>
                <tr>
                    <th>Cód.</th>
                    <th>Nome</th>
                    <th>Nacionalidade</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="artista" items="${listaArtistas}">
                    <tr>
                        <td>${artista.codArtista}</td>
                        <td>${artista.nomeArtista}</td>
                        <td>${artista.nacionalidadae}</td>
                        <%-- Links de editar e excluir agora enviam apenas o 'codArtista' --%>
                        <td><a href="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador?opcao=editar&codArtista=${artista.codArtista}">Editar</a></td>
                        <td><a href="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador?opcao=excluir&codArtista=${artista.codArtista}">Excluir</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>