package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.VisitantesDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.VisitasDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitas;

import com.mycompany.luiz_angelina_museu.servico.ConverteData;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/VisitasControlador")
public class VisitasControlador extends HttpServlet {

    private VisitasDAO visitasDAO;
    private VisitantesDAO visitantesDAO;
    private final ConverteData converte = new ConverteData();

    @Override
    public void init() throws ServletException {
        visitasDAO = new VisitasDAO();
        visitantesDAO = new VisitantesDAO();
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
                    listar(request, response);
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
                    listar(request, response); // Redireciona para a listagem
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codVisita", "0");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisitante = request.getParameter("Visitantes_codVisitante");
        String dataVisita = request.getParameter("dataVisita");

        if (codVisitante == null || codVisitante.isEmpty() || dataVisita == null || dataVisita.isEmpty()) {
            request.setAttribute("mensagem", "Erro: Todos os campos são obrigatórios.");
            encaminharParaPagina(request, response);
            return;
        }

        Visitas visita = new Visitas();
        Visitantes visitante = new Visitantes();
        visitante.setCodVisitante(Integer.parseInt(codVisitante));

        visita.setVisitante(visitante);
        visita.setData(converte.converteCalendario(dataVisita));

        // Como não há DAO para salvar visitas no seu projeto, esta linha precisaria ser implementada
        // visitasDAO.salvar(visita); 
        request.setAttribute("mensagem", "Visita cadastrada com sucesso! (Funcionalidade de salvar a ser implementada no DAO)");
        encaminharParaPagina(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisita = request.getParameter("codVisita");
        Visitas visita = visitasDAO.buscarVisitaPorId(Integer.parseInt(codVisita));

        request.setAttribute("codVisita", visita.getCodVisita());
        request.setAttribute("Visitantes_codVisitante", visita.getVisitante().getCodVisitante());
        request.setAttribute("dataVisita", ConverteData.formataDate(visita.getData()));
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    private void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisita = request.getParameter("codVisita");
        String codVisitante = request.getParameter("Visitantes_codVisitante");
        String dataVisita = request.getParameter("dataVisita");

        Visitas visita = new Visitas();
        visita.setCodVisita(Integer.parseInt(codVisita));

        Visitantes visitante = new Visitantes();
        visitante.setCodVisitante(Integer.parseInt(codVisitante));
        visita.setVisitante(visitante);

        visita.setData(converte.converteCalendario(dataVisita));

        // visitasDAO.alterar(visita);
        request.setAttribute("mensagem", "Visita alterada com sucesso! (Funcionalidade de alterar a ser implementada no DAO)");
        encaminharParaPagina(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codVisita = request.getParameter("codVisita");
        Visitas visita = visitasDAO.buscarVisitaPorId(Integer.parseInt(codVisita));

        request.setAttribute("codVisita", visita.getCodVisita());
        request.setAttribute("Visitantes_codVisitante", visita.getVisitante().getCodVisitante());
        request.setAttribute("dataVisita", ConverteData.formataDate(visita.getData()));
        request.setAttribute("mensagem", "Confirme a exclusão da visita");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codVisita = Integer.parseInt(request.getParameter("codVisita"));
        Visitas visita = new Visitas();
        visita.setCodVisita(codVisita);

        // visitasDAO.excluir(visita);
        request.setAttribute("mensagem", "Visita excluída com sucesso! (Funcionalidade de excluir a ser implementada no DAO)");
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Visitas> listaVisitas = visitasDAO.buscarTodasVisitas();
        List<Visitantes> listaVisitantes = visitantesDAO.buscarTodosVisitantes();

        request.setAttribute("listaVisitas", listaVisitas);
        request.setAttribute("listaVisitantes", listaVisitantes);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroVisitas.jsp");
        dispatcher.forward(request, response);
    }
}
