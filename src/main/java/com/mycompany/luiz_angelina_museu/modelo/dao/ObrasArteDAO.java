package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Artistas;
import com.mycompany.luiz_angelina_museu.modelo.entidade.ObrasArte;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ObrasArteDAO extends GenericoDAO<ObrasArte> {

    public void salvar(ObrasArte obra) {
        String sql = "INSERT INTO obrasarte (nome, descricao, Artistas_codArtista) VALUES (?, ?, ?)";
        save(sql, obra.getNome(), obra.getDescricao(), obra.getArtista().getCodArtista());
    }

    public void alterar(ObrasArte obra) {
        String sql = "UPDATE obrasarte SET nome = ?, descricao = ?, Artistas_codArtista = ? WHERE codObraarte = ?";
        save(sql, obra.getNome(), obra.getDescricao(), obra.getArtista().getCodArtista(), obra.getCodObraarte());
    }

    public void excluir(ObrasArte obra) {
        String sql = "DELETE FROM obrasarte WHERE codObraarte = ?";
        save(sql, obra.getCodObraarte());
    }

    private static class ObrasArteRowMapper implements RowMapper<ObrasArte> {

        ArtistaDAO artistaDAO = new ArtistaDAO();

        @Override
        public ObrasArte mapRow(ResultSet rs) throws SQLException {
            ObrasArte obra = new ObrasArte();
            obra.setCodObraarte(rs.getInt("codObraarte"));
            obra.setNome(rs.getString("nome"));
            obra.setDescricao(rs.getString("descricao"));

            Artistas artista = artistaDAO.buscarArtistasPorId(rs.getInt("Artistas_codArtista"));
            obra.setArtista(artista);

            return obra;
        }
    }

    public List<ObrasArte> buscarTodasObras() {
        String sql = "SELECT * FROM obrasarte";
        return buscarTodos(sql, new ObrasArteRowMapper());
    }

    public ObrasArte buscarObrasPorId(int id) {
        String sql = "SELECT * FROM obrasarte WHERE codObraarte = ?";
        return buscarPorId(sql, new ObrasArteRowMapper(), id);
    }
}
