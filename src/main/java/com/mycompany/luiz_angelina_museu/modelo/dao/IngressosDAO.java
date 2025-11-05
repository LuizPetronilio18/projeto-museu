package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Ingressos;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Museus;
import com.mycompany.luiz_angelina_museu.modelo.entidade.TipoIngresso;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class IngressosDAO {

    private final GenericoDAO<Ingressos> dao = new GenericoDAO<>();
    private final VisitasDAO visitasDAO = new VisitasDAO();
    private final MuseusDAO museusDAO = new MuseusDAO();

   
    public void salvar(Ingressos ingresso) {
        String sql = "INSERT INTO ingressos (Visitas_codVisita, Museus_codMuseu, tipo, preco) VALUES (?, ?, ?, ?)";
        // Converte o Enum para String antes de salvar
        dao.save(sql,
                 ingresso.getVisita().getCodVisita(),
                 ingresso.getMuseu().getCodMuseu(),
                 ingresso.getTipo().name(), // serve para a conversão para string 
                 ingresso.getPreco());
    }


    public void alterar(Ingressos ingresso) {
        String sql = "UPDATE ingressos SET Visitas_codVisita=?, Museus_codMuseu=?, tipo=?, preco=? WHERE codIngresso=?";
        dao.save(sql,
                 ingresso.getVisita().getCodVisita(),
                 ingresso.getMuseu().getCodMuseu(),
                 ingresso.getTipo().name(), // .name()
                 ingresso.getPreco(),
                 ingresso.getCodIngresso());
    }

    // Excluir
    public void excluir(int codIngresso) {
        String sql = "DELETE FROM ingressos WHERE codIngresso=?";
        dao.save(sql, codIngresso);
    }

    // Buscar todos os ingressos
    public List<Ingressos> buscarTodosIngressos() {
        String sql = "SELECT * FROM ingressos";
        return dao.buscarTodos(sql, new IngressosRowMapper());
    }

    // Buscar ingresso por ID
    public Ingressos buscarPorId(int codIngresso) {
        String sql = "SELECT * FROM ingressos WHERE codIngresso=?";
        return dao.buscarPorId(sql, new IngressosRowMapper(), codIngresso);
    }

    private class IngressosRowMapper implements RowMapper<Ingressos> {
        @Override
        public Ingressos mapRow(ResultSet rs) throws SQLException {
            Ingressos ingresso = new Ingressos();
            ingresso.setCodIngresso(rs.getInt("codIngresso"));
            ingresso.setPreco(rs.getDouble("preco"));

            // Converte a String do banco de dados de volta para o Enum
            try {
                ingresso.setTipo(TipoIngresso.valueOf(rs.getString("tipo")));
            } catch (IllegalArgumentException e) {
                // Trata o caso de um valor inesperado no banco de dados
                System.err.println("Valor de 'tipo' inválido no banco de dados: " + rs.getString("tipo"));
                // Pode definir um valor padrão ou lançar uma exceção
                ingresso.setTipo(TipoIngresso.Inteira); // Valor padrão
            }

            Visitas visita = visitasDAO.buscarVisitaPorId(rs.getInt("Visitas_codVisita"));
            Museus museu = museusDAO.buscarMuseuPorId(rs.getInt("Museus_codMuseu"));

            ingresso.setVisita(visita);
            ingresso.setMuseu(museu);

            return ingresso;
        }
    }
}
