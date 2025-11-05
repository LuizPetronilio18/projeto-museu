<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Erro na Aplicação</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilo/estilo.css">
</head>
<body>
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
                    <a href="${pageContext.request.contextPath}${URL_BASE}/VisitasControlador">Visitas</a>
                </div>
            </li>
        </ul>
    </nav>
    <div style="padding: 20px; text-align: center;">
        <h1>Ocorreu um Erro</h1>
        <p style="color: red; font-weight: bold;">
            <%-- Exibe a mensagem de erro passada pelo servlet --%>
            ${requestScope.mensagemErro}
        </p>
        <p>Por favor, tente novamente. Se o problema persistir, contacte o suporte.</p>
        <br>
        <a href="javascript:history.back()">Voltar para a página anterior</a>
    </div>
</body>
</html>
