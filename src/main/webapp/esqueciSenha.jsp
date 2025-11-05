<%-- 
    Document   : esqueciSenha
    Created on : 12 de ago. de 2025, 20:59:13
    Author     : luizo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Esqueci a Minha Senha</title>
    <link rel="stylesheet" type="text/css" href="estilo/estilo.css">
</head>
<body>
    <div class="login-container">
        <h2>Recuperar Senha</h2>
        <p>Por favor, insira o seu e-mail para receber as instruções de recuperação.</p>

        <%-- Exibe a mensagem de sucesso ou erro enviada pelo servlet --%>
        <% if (request.getAttribute("mensagem") != null) { %>
            <p style="color: #0056b3; text-align: center;">${mensagem}</p>
        <% } %>

        <form action="esqueci-senha" method="post">
            <label for="email">E-mail:</label>
            <input type="email" id="email" name="email" required>
            
            <input type="submit" value="Enviar" class="button">
        </form>
        <div style="text-align: center; margin-top: 15px;">
            <a href="login.jsp">Voltar para o Login</a>
        </div>
    </div>
</body>
</html>

