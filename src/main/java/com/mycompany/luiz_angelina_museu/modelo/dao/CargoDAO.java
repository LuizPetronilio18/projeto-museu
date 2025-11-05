package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Cargo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CargoDAO {

    private final GenericoDAO<Cargo> dao = new GenericoDAO<>();

    public void salvar(Cargo cargo) {
        String sql = "INSERT INTO cargo (nomeCargo, salario, turno) VALUES (?, ?, ?)";
        dao.save(sql, cargo.getNomeCargo(), cargo.getSalario(), cargo.getTurno());
    }

    public void alterar(Cargo cargo) {
        String sql = "UPDATE cargo SET nomeCargo = ?, salario = ?, turno = ? WHERE codCargo = ?";
        dao.save(sql, cargo.getNomeCargo(), cargo.getSalario(), cargo.getTurno(), cargo.getCodCargo());
    }

    public void excluir(Cargo cargo) {
        String sql = "DELETE FROM cargo WHERE codCargo = ?";
        dao.save(sql, cargo.getCodCargo());
    }

    public List<Cargo> buscarTodosCargos() {
        String sql = "SELECT * FROM Cargo";
        return dao.buscarTodos(sql, new CargoRowMapper());
    }

    public Cargo buscarCargoPorId(int id) {
        String sql = "SELECT * FROM Cargo WHERE codCargo = ?";
        return dao.buscarPorId(sql, new CargoRowMapper(), id);
    }

    private static class CargoRowMapper implements RowMapper<Cargo> {
        @Override
        public Cargo mapRow(ResultSet rs) throws SQLException {
            Cargo objCargo = new Cargo();
            objCargo.setCodCargo(rs.getInt("codCargo"));
            objCargo.setNomeCargo(rs.getString("nomeCargo"));
            objCargo.setSalario(rs.getDouble("salario"));
            objCargo.setTurno(rs.getString("turno"));
            return objCargo;
        }
    }
}
