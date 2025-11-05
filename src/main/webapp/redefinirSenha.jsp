<%-- 
    Document   : redefinirSenha
    Created on : 12 de ago. de 2025, 21:01:02
    Author     : luizo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Redefinir Senha</title>
    <link rel="stylesheet" type="text/css" href="estilo/estilo.css">
</head>
<body>
    <div class="login-container">
        <h2>Crie a sua Nova Senha</h2>

        <%-- Exibe a mensagem de erro, se houver --%>
        <% if (request.getAttribute("mensagem") != null) { %>
            <p style="color: red; text-align: center;">${mensagem}</p>
        <% } %>

        <form action="redefinir-senha" method="post">
            <!-- Campo oculto para enviar o token de volta para o servlet -->
            <input type="hidden" name="token" value="${token}">

            <label for="senha">Nova Senha:</label>
            <input type="password" id="senha" name="senha" required>

            <label for="confirmarSenha">Confirmar Nova Senha:</label>
            <input type="password" id="confirmarSenha" name="confirmarSenha" required>
            
            <input type="submit" value="Redefinir Senha" class="button">
        </form>
    </div>
</body>
</html>
