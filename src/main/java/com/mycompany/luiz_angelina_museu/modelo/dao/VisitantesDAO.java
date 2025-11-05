package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VisitantesDAO {

    private final GenericoDAO<Visitantes> dao = new GenericoDAO<>();

    public void salvar(Visitantes visitantes) {
        String sql = "INSERT INTO visitantes (nome, emailVisitante, cpf) VALUES (?, ?, ?)";
        dao.save(sql, visitantes.getNome(), visitantes.getEmailVisitante(), visitantes.getCpf());
    }

    public void alterar(Visitantes visitantes) {
        String sql = "UPDATE visitantes SET nome = ?, emailVisitante = ?, cpf = ? WHERE codVisitante = ?";
        dao.save(sql, visitantes.getNome(), visitantes.getEmailVisitante(), visitantes.getCpf(), visitantes.getCodVisitante());
    }

    public void excluir(Visitantes visitantes) {
        String sql = "DELETE FROM visitantes WHERE codVisitante = ?";
        dao.save(sql, visitantes.getCodVisitante());
    }

    public List<Visitantes> buscarTodosVisitantes() {
        String sql = "SELECT * FROM visitantes";
        return dao.buscarTodos(sql, new VisitanteRowMapper());
    }

    // ✅ NOVO MÉTODO: buscar visitante por ID
    public Visitantes buscarVisitantePorId(int id) {
        String sql = "SELECT * FROM visitantes WHERE codVisitante = ?";
        return dao.buscarPorId(sql, new VisitanteRowMapper(), id);
    }

    // ✅ Deixei o RowMapper fora para reaproveitar
    private static class VisitanteRowMapper implements RowMapper<Visitantes> {
        @Override
        public Visitantes mapRow(ResultSet rs) throws SQLException {
            Visitantes visitantes = new Visitantes();
            visitantes.setCodVisitante(rs.getInt("codVisitante"));
            visitantes.setNome(rs.getString("nome"));
            visitantes.setEmailVisitante(rs.getString("emailVisitante"));
            visitantes.setCpf(rs.getString("cpf"));
            return visitantes;
        }
    }
}
