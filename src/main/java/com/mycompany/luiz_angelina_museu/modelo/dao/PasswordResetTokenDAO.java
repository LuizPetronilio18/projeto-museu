/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.PasswordResetToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author luizo
 */
public class PasswordResetTokenDAO {

    public PasswordResetTokenDAO() {
        // Construtor vazio.
    }

    public void save(PasswordResetToken token) {
        String sql = "INSERT INTO password_reset_tokens (usuario_id, token, data_expiracao) VALUES (?, ?, ?)";
        // Usando o padrão Singleton para obter a conexão
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, token.getUsuarioId());
            stmt.setString(2, token.getToken());
            stmt.setTimestamp(3, new Timestamp(token.getDataExpiracao().getTime()));
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o token de redefinição de senha", e);
        }
    }

    public PasswordResetToken findByToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PasswordResetToken tokenEntity = new PasswordResetToken();
                    tokenEntity.setId(rs.getInt("id"));
                    tokenEntity.setToken(rs.getString("token"));
                    tokenEntity.setUsuarioId(rs.getInt("usuario_id"));
                    tokenEntity.setDataExpiracao(rs.getTimestamp("data_expiracao"));
                    return tokenEntity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o token", e);
        }
        return null;
    }

    public void delete(String token) {
        String sql = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o token", e);
        }
    }
}
