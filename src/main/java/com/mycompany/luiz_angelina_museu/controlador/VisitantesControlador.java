package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.VisitantesDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioVisitantes;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/VisitantesControlador")
public class VisitantesControlador extends HttpServlet {

    private VisitantesDAO visitantesDAO;

    @Override
    public void init() throws ServletException {
        visitantesDAO = new VisitantesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                listarVisitantes(request, response);
                return;
            }

            switch (opcao) {
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
                    GeradorRelatorioVisitantes gerador = new GeradorRelatorioVisitantes();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    listarVisitantes(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            encaminharParaPagina(request, response);
        }
    }

    private void listarVisitantes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codVisitante", "0");
        request.setAttribute("nome", "");
        request.setAttribute("emailVisitante", "");
        request.setAttribute("cpf", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String emailVisitante = request.getParameter("emailVisitante");
        String cpf = request.getParameter("cpf");

        // Validações
        if (nome == null || nome.isEmpty()) {
            request.setAttribute("mensagem", "Nome inválido");
            encaminharParaPagina(request, response);
            return;
        }

        if (emailVisitante == null || emailVisitante.isEmpty()) {
            request.setAttribute("mensagem", "E-mail inválido");
            encaminharParaPagina(request, response);
            return;
        }

        if (cpf == null || cpf.isEmpty()) {
            request.setAttribute("mensagem", "CPF inválido");
            encaminharParaPagina(request, response);
            return;
        }

        Visitantes visitante = new Visitantes();
        visitante.setNome(nome);
        visitante.setEmailVisitante(emailVisitante);
        visitante.setCpf(cpf);

        visitantesDAO.salvar(visitante);
        request.setAttribute("mensagem", "Visitante cadastrado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisitante = request.getParameter("codVisitante");
        Visitantes visitante = visitantesDAO.buscarVisitantePorId(Integer.parseInt(codVisitante));

        request.setAttribute("codVisitante", codVisitante);
        request.setAttribute("nome", visitante.getNome());
        request.setAttribute("emailVisitante", visitante.getEmailVisitante());
        request.setAttribute("cpf", visitante.getCpf());
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    private void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisitante = request.getParameter("codVisitante");
        String nome = request.getParameter("nome");
        String emailVisitante = request.getParameter("emailVisitante");
        String cpf = request.getParameter("cpf");

        Visitantes visitante = new Visitantes();
        visitante.setCodVisitante(Integer.parseInt(codVisitante));
        visitante.setNome(nome);
        visitante.setEmailVisitante(emailVisitante);
        visitante.setCpf(cpf);

        visitantesDAO.alterar(visitante);
        request.setAttribute("mensagem", "Visitante atualizado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisitante = request.getParameter("codVisitante");
        Visitantes visitante = visitantesDAO.buscarVisitantePorId(Integer.parseInt(codVisitante));

        request.setAttribute("codVisitante", codVisitante);
        request.setAttribute("nome", visitante.getNome());
        request.setAttribute("emailVisitante", visitante.getEmailVisitante());
        request.setAttribute("cpf", visitante.getCpf());
        request.setAttribute("mensagem", "Confirme a exclusão");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisitante = request.getParameter("codVisitante");
        Visitantes visitante = new Visitantes();
        visitante.setCodVisitante(Integer.parseInt(codVisitante));

        visitantesDAO.excluir(visitante);
        request.setAttribute("mensagem", "Visitante excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codVisitante", "0");
        request.setAttribute("nome", "");
        request.setAttribute("emailVisitante", "");
        request.setAttribute("cpf", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Visitantes> listaVisitantes = visitantesDAO.buscarTodosVisitantes();
        request.setAttribute("listaVisitantes", listaVisitantes);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroVisitantes.jsp");
        dispatcher.forward(request, response);
    }
}
