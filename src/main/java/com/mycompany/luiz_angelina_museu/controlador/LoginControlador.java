package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.UsuarioDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Usuario;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(WebConstante.BASE_PATH + "/LoginControlador")
public class LoginControlador extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        if (opcao == null) {
            response.getWriter().println("Erro: Opção não especificada.");
            return;
        }

        try {
            switch (opcao) {
                case "cadastrar":
                    cadastrar(request, response);
                    break;
                case "login":
                    login(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Erro: " + e.getMessage());
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Usuario usuario = usuarioDAO.buscarUsuarioPorNomeUsuario(username);

        if (usuario != null && BCrypt.checkpw(password, usuario.getSenha())) {
            request.getSession().setAttribute("user", username);
            response.sendRedirect(request.getContextPath() + "/homepage.jsp");
        } else {
            request.setAttribute("mensagem", "Usuário ou senha inválidos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        // VERIFICAÇÃO ADICIONADA: Verifica se o utilizador ou e-mail já existem
        if (usuarioDAO.buscarUsuarioPorNomeUsuario(username) != null || usuarioDAO.findByEmail(email) != null) {
            request.setAttribute("mensagem", "Erro: Nome de usuário ou e-mail já existem.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        usuarioDAO.registrarUsuario(username, password, email);
        request.setAttribute("mensagem", "Usuário cadastrado com sucesso!");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.setAttribute("mensagem", "Sessão encerrada com sucesso.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }
}
