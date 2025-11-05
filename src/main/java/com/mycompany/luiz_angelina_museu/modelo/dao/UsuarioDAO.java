package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO extends GenericoDAO<Usuario> {

    public Usuario buscarUsuarioPorNomeUsuario(String nomeUsuario) {
        String sql = "SELECT id_usuario, nome, senha, email FROM usuario WHERE nome = ?";
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registrarUsuario(String nomeUsuario, String senhaSimples, String email) {
        String hashDeSenha = BCrypt.hashpw(senhaSimples, BCrypt.gensalt());
        String sql = "INSERT INTO usuario (nome, senha, email) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);
            stmt.setString(2, hashDeSenha);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario findByEmail(String email) {
        String sql = "SELECT id_usuario, nome, senha, email FROM usuario WHERE email = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setEmail(rs.getString("email"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail", e);
        }
        return null;
    }

    public void updatePassword(int userId, String newPassword) {
        String hashDeSenha = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE usuario SET senha = ? WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashDeSenha);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a senha do usuário", e);
        }
    }
}
