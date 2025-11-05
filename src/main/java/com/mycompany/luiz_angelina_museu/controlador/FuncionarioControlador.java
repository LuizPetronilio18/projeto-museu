package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.CargoDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.FuncionarioDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Cargo;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Funcionario;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioFuncionarios;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/FuncionarioControlador")
public class FuncionarioControlador extends HttpServlet {

    private FuncionarioDAO objFuncionarioDao;
    private CargoDAO objcargoDao;

    @Override
    public void init() throws ServletException {
        objcargoDao = new CargoDAO();
        objFuncionarioDao = new FuncionarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "listar";
            }

            switch (opcao) {
                case "listar":
                    encaminharParapagina(request, response);
                    break;
                case "cadastrar":
                    cadastrar(request, response);
                    break;
                case "editar":
                    editar(request, response);
                    break;
                case "confirmarEditar":
                    confirmarEditar(request, response);
                    break;
                case "excluir":
                    excluir(request, response);
                    break;
                case "confirmarExcluir":
                    confirmarExcluir(request, response);
                    break;
                case "gerarRelatorio":
                    GeradorRelatorioFuncionarios gerador = new GeradorRelatorioFuncionarios();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    throw new IllegalAccessException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            response.getWriter().println("Erro: " + e.getMessage());
        }
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String cargo_codCargo = request.getParameter("cargo_codCargo");

        Funcionario objFuncionario = new Funcionario();
        objFuncionario.setNome(nome);
        objFuncionario.setCpf(cpf);

        Cargo cargo = new Cargo();
        cargo.setCodCargo(Integer.parseInt(cargo_codCargo));
        objFuncionario.setCargo_codCargo(cargo);

        objFuncionarioDao.salvar(objFuncionario);
        encaminharParapagina(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codFuncionario", request.getParameter("codFuncionario"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("cpf", request.getParameter("cpf"));
        request.setAttribute("cargo_codCargo", request.getParameter("cargo_codCargo"));
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParapagina(request, response);
    }

    private void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codFuncionario = request.getParameter("codFuncionario");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String cargo_codCargo = request.getParameter("cargo_codCargo");

        Funcionario objFuncionario = new Funcionario();
        objFuncionario.setCodFuncionario(Integer.parseInt(codFuncionario));
        objFuncionario.setNome(nome);
        objFuncionario.setCpf(cpf);

        Cargo cargo = new Cargo();
        cargo.setCodCargo(Integer.parseInt(cargo_codCargo));
        objFuncionario.setCargo_codCargo(cargo);

        objFuncionarioDao.alterar(objFuncionario);
        encaminharParapagina(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codFuncionario", request.getParameter("codFuncionario"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("cpf", request.getParameter("cpf"));
        request.setAttribute("cargo_codCargo", request.getParameter("cargo_codCargo"));
        request.setAttribute("mensagem", "Clique em salvar para excluir");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParapagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codFuncionario = request.getParameter("codFuncionario");
        Funcionario objFuncionario = new Funcionario();
        objFuncionario.setCodFuncionario(Integer.parseInt(codFuncionario));
        objFuncionarioDao.excluir(objFuncionario);
        encaminharParapagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codFuncionario", "0");
        request.setAttribute("nome", "");
        request.setAttribute("cpf", "");
        request.setAttribute("cargo_codCargo", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParapagina(request, response);
    }

    private void encaminharParapagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cargo> listaCargo = objcargoDao.buscarTodosCargos();
        request.setAttribute("listaCargo", listaCargo);

        List<Funcionario> listaFuncionario = objFuncionarioDao.buscarTodosFuncionarios();
        request.setAttribute("listaFuncionario", listaFuncionario);

        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroFuncionario.jsp");
        enviar.forward(request, response);
    }
}
