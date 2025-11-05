package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Doacoes;
import com.mycompany.luiz_angelina_museu.servico.ConverteData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DoacoesDAO extends GenericoDAO<Doacoes> {

    public void salvar(Doacoes objDoacoes) {
        String sql = "INSERT INTO DOACOES (VALORDOACAO, NOMEDOACAO, DATADOACAO, Museus_codMuseu) VALUES (?, ?, ?, ?)";
        save(sql,
            objDoacoes.getValorDoacao(),
            objDoacoes.getNomeDoacao(),
            objDoacoes.getDataDoacao(),  // Calendar pode precisar de conversão no método save()
            objDoacoes.getMuseu().getCodMuseu()  // Aqui: getMuseu()
        );
    }

    public void alterar(Doacoes objDoacoes) {
        String sql = "UPDATE DOACOES SET VALORDOACAO = ?, NOMEDOACAO = ?, DATADOACAO = ?, Museus_codMuseu = ? WHERE CODDOACAO = ?";
        save(sql,
            objDoacoes.getValorDoacao(),
            objDoacoes.getNomeDoacao(),
            objDoacoes.getDataDoacao(),  // idem acima
            objDoacoes.getMuseu().getCodMuseu(),  // getMuseu()
            objDoacoes.getCodDoacao()
        );
    }

    public void excluir(Doacoes objDoacoes) {
        String sql = "DELETE FROM DOACOES WHERE CODDOACAO = ?";
        save(sql, objDoacoes.getCodDoacao());
    }

    private static class DoacoesRowMapper implements RowMapper<Doacoes> {
        ConverteData converte = new ConverteData();
        MuseusDAO objMuseuDao = new MuseusDAO();

        @Override
        public Doacoes mapRow(ResultSet rs) throws SQLException {
            Doacoes objDoacoes = new Doacoes();
            objDoacoes.setCodDoacao(rs.getInt("CODDOACAO"));
            objDoacoes.setNomeDoacao(rs.getString("NOMEDOACAO"));
            objDoacoes.setValorDoacao(rs.getDouble("VALORDOACAO"));
            objDoacoes.setDataDoacao(converte.converteCalendario(rs.getDate("DATADOACAO")));
            objDoacoes.setMuseu(  // ajustado para setMuseu()
                objMuseuDao.buscarMuseuPorId(rs.getInt("Museus_codMuseu"))
            );
            return objDoacoes;
        }
    }

    public List<Doacoes> buscarTodasDoacoes() {
        String sql = "SELECT * FROM DOACOES";
        return buscarTodos(sql, new DoacoesRowMapper());
    }

    public Doacoes buscarDoacoesPorId(int id) {
        String sql = "SELECT * FROM DOACOES WHERE CODDOACAO = ?";
        return buscarPorId(sql, new DoacoesRowMapper(), id);
    }
}
