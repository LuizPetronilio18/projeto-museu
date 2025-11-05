package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Museus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Luiz
 */
public class MuseusDAO {

    private final GenericoDAO<Museus> dao = new GenericoDAO<>();

    // Salvar
    public void salvar(Museus museus) {
        String sql = "INSERT INTO museus (nome, localizacao, horariofuncionamento, descricao, cnpj) VALUES (?, ?, ?, ?, ?)";
        dao.save(sql, museus.getNome(), museus.getLocalizacao(), museus.getHorariofuncionamento(), museus.getDescricao(), museus.getCnpj());
    }

    // Alterar
    public void alterar(Museus museus) {
        String sql = "UPDATE museus SET nome = ?, localizacao = ?, horariofuncionamento = ?, descricao = ?, cnpj = ? WHERE codMuseu = ?";
        dao.save(sql, museus.getNome(), museus.getLocalizacao(), museus.getHorariofuncionamento(), museus.getDescricao(), museus.getCnpj(), museus.getCodMuseu());
    }

    // Excluir
    public void excluir(Museus museus) {
        String sql = "DELETE FROM museus WHERE codMuseu = ?";
        dao.save(sql, museus.getCodMuseu());
    }

    // Buscar todos os museus
    public List<Museus> buscarTodosMuseus() {
        String sql = "SELECT * FROM museus";
        return dao.buscarTodos(sql, new MuseuRowMapper());
    }

    // Buscar por ID
    public Museus buscarMuseuPorId(int id) {
        String sql = "SELECT * FROM museus WHERE codMuseu = ?";
        return dao.buscarPorId(sql, new MuseuRowMapper(), id);
    }

    // RowMapper interno
    private static class MuseuRowMapper implements RowMapper<Museus> {
        @Override
        public Museus mapRow(ResultSet rs) throws SQLException {
            Museus museu = new Museus();
            museu.setCodMuseu(rs.getInt("codMuseu"));
            museu.setNome(rs.getString("nome"));
            museu.setLocalizacao(rs.getString("localizacao"));
            museu.setHorariofuncionamento(rs.getString("horariofuncionamento"));
            museu.setDescricao(rs.getString("descricao"));
            museu.setCnpj(rs.getString("cnpj"));
            return museu;
        }
    }
}
