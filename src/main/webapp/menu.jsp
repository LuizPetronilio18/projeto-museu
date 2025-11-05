<style>
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        margin: 0;
        background-color: #f4f7fc;
    }
    .container {
        padding: 20px;
        max-width: 1200px;
        margin: auto;
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
        z-index: 1000;
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

    /* Adicione este estilo para o novo botão de relatório */
    button, .btn {
        padding: 10px 15px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        color: white;
        font-size: 16px;
        text-decoration: none;
        display: inline-block;
        background-color: #3498db; /* Azul */
    }
    button:hover, .btn:hover {
        background-color: #2980b9;
    }
</style>
<nav>
    <ul>
        <li><a href="${pageContext.request.contextPath}/homepage.jsp">Início</a></li>

        <li class="dropdown">
            <a href="javascript:void(0)" class="dropbtn">Cadastros</a>
            <div class="dropdown-content">
                <a href="${pageContext.request.contextPath}${URL_BASE}/ArtistaControlador">Artistas</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/CargoControlador">Cargos</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/DoacoesControlador">Doações</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/EventosControlador">Eventos</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/ExposicaoControlador">Exposições</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador">Funcionários</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/IngressosControlador">Vender Ingressos</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/MuseusControlador">Museus</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/ObrasArteControlador">Obras de Arte</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/VisitantesControlador">Visitantes</a>
            </div>
        </li>

   

        <li style="float: right;"><a href="${pageContext.request.contextPath}${URL_BASE}/LoginControlador?opcao=logout">Logout</a></li>
    </ul>
</nav>