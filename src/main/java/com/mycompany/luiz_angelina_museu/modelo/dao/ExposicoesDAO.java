package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Exposicoes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ExposicoesDAO extends GenericoDAO<Exposicoes> {

    public void salvar(Exposicoes objExposicoes) {
        String sql = "INSERT INTO exposicoes(nome,sobreExposicao) VALUES(?,?)";
        save(sql, objExposicoes.getNome(), objExposicoes.getSobreExposicao());
    }

    public void alterar(Exposicoes objExposicoes) {
        String sql = "UPDATE exposicoes SET nome=?, sobreExposicao=? WHERE codExposicao=?";
        save(sql, objExposicoes.getNome(), objExposicoes.getSobreExposicao(), objExposicoes.getCodExposicao());
    }

    public void excluir(Exposicoes objExposicoes) {
        String sql = "DELETE FROM exposicoes WHERE codExposicao=?";
        save(sql, objExposicoes.getCodExposicao());
    }

    // Adaptado corretamente para chamar o método da superclasse
    public List<Exposicoes> buscarTodasExposicao() {
        String sql = "SELECT * FROM exposicoes";
        return super.buscarTodos(sql, new ExposicaoRowMapper());
    }

    private static class ExposicaoRowMapper implements RowMapper<Exposicoes> {
        @Override
        public Exposicoes mapRow(ResultSet rs) throws SQLException {
            Exposicoes objExposicoes = new Exposicoes();
            objExposicoes.setCodExposicao(rs.getInt("codExposicao"));
            objExposicoes.setNome(rs.getString("nome"));
            objExposicoes.setSobreExposicao(rs.getString("sobreExposicao"));
            return objExposicoes;
        }
    }
}
