package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Artistas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ArtistaDAO {

    private final GenericoDAO<Artistas> dao = new GenericoDAO<>();

    public void salvar(Artistas artistas) {
        String sql = "INSERT INTO artistas (nomeArtista, nacionalidadae) VALUES (?, ?)";
        dao.save(sql, artistas.getNomeArtista(), artistas.getNacionalidadae());
    }

    public void alterar(Artistas artistas) {
        String sql = "UPDATE artistas SET nomeArtista = ?, nacionalidadae = ? WHERE codArtista = ?";
        dao.save(sql, artistas.getNomeArtista(), artistas.getNacionalidadae(), artistas.getCodArtista());
    }

    public void excluir(Artistas artistas) {
        String sql = "DELETE FROM artistas WHERE codArtista = ?";
        dao.save(sql, artistas.getCodArtista());
    }

    private static class ArtistasRowMapper implements RowMapper<Artistas> {
        @Override
        public Artistas mapRow(ResultSet rs) throws SQLException {
            Artistas objArtistas = new Artistas();
            objArtistas.setCodArtista(rs.getInt("codArtista"));
            objArtistas.setNomeArtista(rs.getString("nomeArtista"));
            objArtistas.setNacionalidadae(rs.getString("nacionalidadae"));
            return objArtistas;
        }
    }

    public List<Artistas> buscarTodosArtistas() {
        String sql = "SELECT * FROM artistas";
        return dao.buscarTodos(sql, new ArtistasRowMapper());
    }

    public Artistas buscarArtistasPorId(int id) {
        String sql = "SELECT * FROM artistas WHERE codArtista = ?";
        return dao.buscarPorId(sql, new ArtistasRowMapper(), id);
    }
}
