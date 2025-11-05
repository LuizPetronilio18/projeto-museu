<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestão de Museus</title>
   
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            background-color: #f4f7fc;
            color: #333;
        }
        .container {
            padding: 20px;
        }

        nav {
            background-color: #2c3e50;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        nav ul {
            list-style-type: none;
            margin: 0;
            padding: 0;
        }
        nav ul li {
            float: left;
        }
        nav ul li a, .dropbtn {
            display: inline-block;
            color: white;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        nav ul li a:hover, .dropdown:hover .dropbtn {
            background-color: #34495e;
        }
        li.dropdown {
            display: inline-block;
        }
        .dropdown-content {
            display: none;
            position: absolute;
            background-color: #f9f9f9;
            min-width: 200px;
            box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
            z-index: 1;
        }
        .dropdown-content a {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
            text-align: left;
        }
        .dropdown-content a:hover {
            background-color: #ddd;
        }
        .dropdown:hover .dropdown-content {
            display: block;
        }
        /* Estilo do Header (Cabeçalho) */
        .header {
            background-color: #3498db;
            color: white;
            padding: 60px 20px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 2.5em;
        }
        .header p {
            font-size: 1.2em;
        }
        /* Estilo dos Cartões de Acesso Rápido */
        .card-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 20px;
            padding: 20px 0;
        }
        .card {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 280px;
            text-align: center;
            padding: 20px;
            transition: transform 0.3s, box-shadow 0.3s;
            text-decoration: none;
            color: inherit;
        }
        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
        }
        .card i {
            font-size: 3em;
            color: #3498db;
        }
        .card h3 {
            margin: 15px 0 10px;
            color: #2c3e50;
        }
        .card p {
            font-size: 0.9em;
            color: #7f8c8d;
        }
        /* Rodapé */
        footer {
            text-align: center;
            padding: 20px;
            background-color: #2c3e50;
            color: white;
            position: relative;
            bottom: 0;
            width: 100%;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
    <%@include file="menu.jsp" %>

    <header class="header">
        <h1>Sistema de Gestão de Museus</h1>
        <p>Gerencie todas as operações do seu museu de forma centralizada e eficiente.</p>
    </header>

    <div class="container">
        <h2 style="text-align: center; margin-bottom: 30px;">Acesso Rápido</h2>
        <div class="card-container">
            <a href="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador" class="card">
                <i class="fas fa-users"></i>
                <h3>Visitantes</h3>
                <p>Gerencie o cadastro de todos os visitantes do museu.</p>
            </a>
            <a href="${pageContext.request.contextPath}${URL_BASE}/ObrasArteControlador" class="card">
                <i class="fas fa-palette"></i>
                <h3>Obras de Arte</h3>
                <p>Cadastre e organize as obras do acervo e seus artistas.</p>
            </a>
            <a href="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador" class="card">
                <i class="fas fa-ticket-alt"></i>
                <h3>Ingressos</h3>
                <p>Controle a venda e o registro de ingressos e visitas.</p>
            </a>
             <a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador" class="card">
                <i class="fas fa-id-badge"></i>
                <h3>Funcionários</h3>
                <p>Administre os funcionários e seus respectivos cargos.</p>
            </a>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 Sistema de Gestão de Museus. Todos os direitos reservados.</p>
    </footer>
</body>
</html>
