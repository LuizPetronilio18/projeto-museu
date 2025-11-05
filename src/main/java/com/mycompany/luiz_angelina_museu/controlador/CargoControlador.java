package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.CargoDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Cargo;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioCargos;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/CargoControlador")
public class CargoControlador extends HttpServlet {
    
    // O DAO pode ser uma variável de instância, pois não guarda estado específico da requisição.
    private CargoDAO objcargoDao;

    @Override
    public void init() throws ServletException {
        objcargoDao = new CargoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        if (opcao == null || opcao.isEmpty()) {
            opcao = "listar"; // Ação padrão para carregar a página
        }

        try {
            switch (opcao) {
                case "listar":
                    encaminharParaPagina(request, response);
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
                case "cancelar":
                    encaminharParaPagina(request, response);
                    break;
                    case "gerarRelatorio":
                GeradorRelatorioCargos gerador = new GeradorRelatorioCargos();
                gerador.geraRelatorio(response);
                break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            request.setAttribute("mensagemErro", "Erro: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // As variáveis agora são locais para este método.
        String nomeCargo = request.getParameter("nomeCargo");
        String turno = request.getParameter("turno");
        double salario = Double.parseDouble(request.getParameter("salario"));

        // Cria um novo objeto Cargo para esta requisição específica.
        Cargo novoCargo = new Cargo();
        novoCargo.setNomeCargo(nomeCargo);
        novoCargo.setSalario(salario);
        novoCargo.setTurno(turno);
        
        objcargoDao.salvar(novoCargo);
        
        request.setAttribute("mensagem", "Cargo cadastrado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codCargo = request.getParameter("codCargo");
        String nomeCargo = request.getParameter("nomeCargo");
        String turno = request.getParameter("turno");
        String salario = request.getParameter("salario");

        request.setAttribute("codCargo", codCargo);
        request.setAttribute("nomeCargo", nomeCargo);
        request.setAttribute("salario", salario);
        request.setAttribute("turno", turno);
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        
        encaminharParaPagina(request, response);
    }

    private void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codCargo = Integer.parseInt(request.getParameter("codCargo"));
        String nomeCargo = request.getParameter("nomeCargo");
        String turno = request.getParameter("turno");
        double salario = Double.parseDouble(request.getParameter("salario"));

        Cargo cargoEditado = new Cargo();
        cargoEditado.setCodCargo(codCargo);
        cargoEditado.setNomeCargo(nomeCargo);
        cargoEditado.setSalario(salario);
        cargoEditado.setTurno(turno);
        
        objcargoDao.alterar(cargoEditado);
        
        request.setAttribute("mensagem", "Cargo alterado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codCargo = request.getParameter("codCargo");
        String nomeCargo = request.getParameter("nomeCargo");
        
        request.setAttribute("codCargo", codCargo);
        request.setAttribute("nomeCargo", nomeCargo);
        request.setAttribute("mensagem", "Confirma a exclusão do cargo '" + nomeCargo + "'?");
        request.setAttribute("opcao", "confirmarExcluir");
        
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codCargo = Integer.parseInt(request.getParameter("codCargo"));
        
        Cargo cargoParaExcluir = new Cargo();
        cargoParaExcluir.setCodCargo(codCargo);
        
        objcargoDao.excluir(cargoParaExcluir);
        
        request.setAttribute("mensagem", "Cargo excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cargo> listaCargo = objcargoDao.buscarTodosCargos();
        request.setAttribute("listaCargo", listaCargo);
        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroCargo.jsp");
        enviar.forward(request, response);
    }
}