/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.PasswordResetTokenDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.UsuarioDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.PasswordResetToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 *
 * @author luizo
 */
@WebServlet(name = "RedefinirSenhaControlador", urlPatterns = {"/redefinir-senha"})
public class RedefinirSenhaControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.findByToken(token);

        // Verifica se o token é nulo ou se a data de expiração já passou
        if (prt == null || prt.getDataExpiracao().before(new Timestamp(System.currentTimeMillis()))) {
            request.setAttribute("mensagem", "Token inválido ou expirado. Por favor, solicite uma nova redefinição de senha.");
            request.getRequestDispatcher("esqueciSenha.jsp").forward(request, response);
            return;
        }

        // Se o token for válido, encaminha para a página de redefinição
        request.setAttribute("token", token);
        request.getRequestDispatcher("redefinirSenha.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.findByToken(token);

        // Valida o token novamente
        if (prt == null || prt.getDataExpiracao().before(new Timestamp(System.currentTimeMillis()))) {
            request.setAttribute("mensagem", "Token inválido ou expirado.");
            request.getRequestDispatcher("esqueciSenha.jsp").forward(request, response);
            return;
        }

        // Valida se as senhas conferem
        if (senha == null || senha.isEmpty() || !senha.equals(confirmarSenha)) {
            request.setAttribute("token", token);
            request.setAttribute("mensagem", "As senhas não conferem ou estão em branco.");
            request.getRequestDispatcher("redefinirSenha.jsp").forward(request, response);
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            // O método updatePassword já faz o hash da nova senha
            usuarioDAO.updatePassword(prt.getUsuarioId(), senha);
            
            // Invalida o token após o uso para que não possa ser usado novamente
            tokenDAO.delete(token);

            request.setAttribute("mensagem", "Senha redefinida com sucesso! Você já pode fazer o login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("token", token);
            request.setAttribute("mensagem", "Ocorreu um erro ao redefinir sua senha.");
            request.getRequestDispatcher("redefinirSenha.jsp").forward(request, response);
        }
    }
}