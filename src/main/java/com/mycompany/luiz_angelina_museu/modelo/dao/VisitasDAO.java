package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitas;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class VisitasDAO extends GenericoDAO<Visitas> {

    private final VisitantesDAO visitantesDAO = new VisitantesDAO();

    public void salvar(Visitas visita) {
        String sql = "INSERT INTO visitas (Visitantes_codVisitante, dataVisita) VALUES (?, ?)";
        save(sql, visita.getVisitante().getCodVisitante(), visita.getData());
    }

    public void alterar(Visitas visita) {
        String sql = "UPDATE visitas SET Visitantes_codVisitante = ?, dataVisita = ? WHERE codVisita = ?";
        save(sql, visita.getVisitante().getCodVisitante(), visita.getData(), visita.getCodVisita());
    }

    public void excluir(Visitas visita) {
        String sql = "DELETE FROM visitas WHERE codVisita = ?";
        save(sql, visita.getCodVisita());
    }

    public Visitas buscarVisitaPorId(int codVisita) {
        String sql = "SELECT * FROM visitas WHERE codVisita=?";
        return buscarPorId(sql, new VisitasRowMapper(), codVisita);
    }
    
    public List<Visitas> buscarTodasVisitas() {
        String sql = "SELECT * FROM visitas";
        return buscarTodos(sql, new VisitasRowMapper());
    }

    private class VisitasRowMapper implements RowMapper<Visitas> {
        @Override
        public Visitas mapRow(ResultSet rs) throws SQLException {
            Visitas visita = new Visitas();
            visita.setCodVisita(rs.getInt("codVisita"));

            java.sql.Date sqlDate = rs.getDate("dataVisita");
            if (sqlDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(sqlDate);
                visita.setData(cal);
            }

            Visitantes visitante = visitantesDAO.buscarVisitantePorId(rs.getInt("Visitantes_codVisitante"));
            visita.setVisitante(visitante);

            return visita;
        }
    }
}
