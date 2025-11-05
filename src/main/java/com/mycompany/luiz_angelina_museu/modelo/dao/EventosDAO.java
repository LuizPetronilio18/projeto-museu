package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Artistas;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Cargo;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Eventos;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EventosDAO {

    private final GenericoDAO<Eventos> dao = new GenericoDAO<>();

    public void salvar(Eventos eventos) {
        String sql = "INSERT INTO eventos (nomeEvento, dataEvento) VALUES (?, ?)";
        dao.save(sql, eventos.getNomeEvento(), new java.sql.Date(eventos.getDataEvento().getTimeInMillis()));
    }

    public void alterar(Eventos eventos) {
        String sql = "UPDATE eventos SET nomeEvento = ?, dataEvento = ? WHERE codEvento = ?";
        dao.save(sql, eventos.getNomeEvento(), new java.sql.Date(eventos.getDataEvento().getTimeInMillis()), eventos.getCodEvento());
    }

    public void excluir(Eventos eventos) {
        String sql = "DELETE FROM eventos WHERE codEvento = ?";
        dao.save(sql, eventos.getCodEvento());
    }

    public List<Eventos> buscarTodosEventos() {
        String sql = "SELECT * FROM eventos";
        return dao.buscarTodos(sql, new RowMapper<Eventos>() {
            @Override
            public Eventos mapRow(ResultSet rs) throws SQLException {
                Eventos eventos = new Eventos();
                eventos.setCodEvento(rs.getInt("codEvento"));
                eventos.setNomeEvento(rs.getString("nomeEvento")); // Corrigido também, o nome da coluna no seu insert é nomeEvento
                Date sqlDate = rs.getDate("dataEvento");
                if (sqlDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sqlDate);
                    eventos.setDataEvento(cal);
                }
                return eventos;
            }
        });
    }
}
