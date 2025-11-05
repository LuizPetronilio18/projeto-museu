/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.PasswordResetTokenDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.UsuarioDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.PasswordResetToken;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Usuario;
import com.mycompany.luiz_angelina_museu.servico.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author luizo
 */
@WebServlet(name = "EsqueciSenhaControlador", urlPatterns = {"/esqueci-senha"})
public class EsqueciSenhaControlador extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            request.setAttribute("mensagem", "Erro: E-mail não encontrado em nosso sistema.");
            request.getRequestDispatcher("esqueciSenha.jsp").forward(request, response);
            return;
        }

        try {
            String token = UUID.randomUUID().toString();
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(token);
            prt.setUsuarioId(usuario.getId_usuario());

            PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
            tokenDAO.save(prt);

            EmailUtil.sendPasswordResetEmail(email, token);
            
            request.setAttribute("mensagem", "Sucesso! Um e-mail com as instruções para redefinir sua senha foi enviado para " + email);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: Não foi possível enviar o e-mail de redefinição. Tente novamente mais tarde.");
        }
        
        request.getRequestDispatcher("esqueciSenha.jsp").forward(request, response);
    }
}

