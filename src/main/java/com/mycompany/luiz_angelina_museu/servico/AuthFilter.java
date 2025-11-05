package com.mycompany.luiz_angelina_museu.servico;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*") // Aplica o filtro a todas as requisições
public class AuthFilter implements Filter {

    // Lista de páginas e recursos públicos que NUNCA serão bloqueados
    private static final List<String> PAGINAS_PUBLICAS = Arrays.asList(
            "/login.jsp",
            "/LoginControlador",
            "/CadastroUsuario.jsp",
            "/esqueciSenha.jsp",
            "/redefinirSenha.jsp",
            "/esqueci-senha",      // URL do servlet
            "/redefinir-senha"     // URL do servlet
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean isPaginaPublica = PAGINAS_PUBLICAS.stream().anyMatch(path::endsWith);
        boolean isRecursoEstatico = path.startsWith("/estilo/");

        boolean isLogado = (session != null && session.getAttribute("user") != null);

        if (isPaginaPublica || isRecursoEstatico || isLogado) {
            // Se a página for pública, um recurso estático, ou se o utilizador estiver logado, permite o acesso
            chain.doFilter(request, response);
        } else {
            // Se não, redireciona para a página de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
