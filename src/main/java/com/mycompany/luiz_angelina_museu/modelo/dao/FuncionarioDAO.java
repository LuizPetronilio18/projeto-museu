package com.mycompany.luiz_angelina_museu.modelo.dao;

import com.mycompany.luiz_angelina_museu.modelo.entidade.Cargo;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Funcionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioDAO extends GenericoDAO<Funcionario> {

    public void salvar(Funcionario objFuncionario) {
        String sql = "INSERT INTO FUNCIONARIOS (NOME, CPF, cargo_codCargo) VALUES (?, ?, ?)";
        save(sql, objFuncionario.getNome(), objFuncionario.getCpf(), objFuncionario.getCargo_codCargo().getCodCargo());
    }

    public void alterar(Funcionario objFuncionario) {
        String sql = "UPDATE FUNCIONARIOS SET NOME=?, CPF=?, cargo_codCargo=? WHERE CODFUNCIONARIO=?";
        save(sql, objFuncionario.getNome(), objFuncionario.getCpf(), objFuncionario.getCargo_codCargo().getCodCargo(), objFuncionario.getCodFuncionario());
    }

    public void excluir(Funcionario objFuncionario) {
        String sql = "DELETE FROM FUNCIONARIOS WHERE CODFUNCIONARIO=?";
        save(sql, objFuncionario.getCodFuncionario());
    }

    public List<Funcionario> buscarTodosFuncionarios() {
        String sql = "SELECT * FROM FUNCIONARIOS";
        return buscarTodos(sql, new FuncionarioRowMapper());
    }

    public Funcionario buscarFuncionarioPorId(int id) {
        String sql = "SELECT * FROM FUNCIONARIOS WHERE codFuncionario=?";
        return buscarPorId(sql, new FuncionarioRowMapper(), id);
    }

    private static class FuncionarioRowMapper implements RowMapper<Funcionario> {
        CargoDAO objCargoDao = new CargoDAO();

        @Override
        public Funcionario mapRow(ResultSet rs) throws SQLException {
            Funcionario objFuncionario = new Funcionario();
            objFuncionario.setCodFuncionario(rs.getInt("codFuncionario"));
            objFuncionario.setNome(rs.getString("nome"));
            objFuncionario.setCpf(rs.getString("cpf"));

            // Corrigido: pegar o cargo pelo campo certo da tabela
            objFuncionario.setCargo_codCargo(objCargoDao.buscarCargoPorId(rs.getInt("cargo_codCargo")));

            return objFuncionario;
        }
    }
}
