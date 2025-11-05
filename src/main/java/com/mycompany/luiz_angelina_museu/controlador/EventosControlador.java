package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.EventosDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Eventos;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioEventos;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/EventosControlador")
public class EventosControlador extends HttpServlet {

    private EventosDAO eventosDao;

    @Override
    public void init() throws ServletException {
        eventosDao = new EventosDAO();
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
                    GeradorRelatorioEventos gerador = new GeradorRelatorioEventos();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    throw new IllegalAccessException("Opção inválida " + opcao);
            }

        } catch (NumberFormatException e) {
            response.getWriter().println("Erro: parâmetros inválidos - " + e.getMessage());
        } catch (IllegalAccessException e) {
            response.getWriter().println("Erro: " + e.getMessage());
        }
    }

    protected void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeEvento = request.getParameter("nomeEvento");
        String dataEvento = request.getParameter("dataEvento");

        if (nomeEvento == null || nomeEvento.trim().isEmpty() || dataEvento == null || dataEvento.trim().isEmpty()) {
            request.setAttribute("mensagem", "Erro: Todos os campos devem ser preenchidos.");
            request.setAttribute("opcao", "cadastrar");
            encaminharParapagina(request, response);
            return;
        }

        Eventos evento = new Eventos();
        evento.setNomeEvento(nomeEvento);
        evento.setDataEvento(converterStringParaCalendar(dataEvento));

        eventosDao.salvar(evento);
        encaminharParapagina(request, response);
    }

    protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codEvento = request.getParameter("codEvento");
        String nomeEvento = request.getParameter("nomeEvento");
        String dataEvento = request.getParameter("dataEvento");

        request.setAttribute("codEvento", codEvento);
        request.setAttribute("nomeEvento", nomeEvento);
        request.setAttribute("dataEvento", dataEvento);
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");

        encaminharParapagina(request, response);
    }

    protected void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codEvento = request.getParameter("codEvento");
        String nomeEvento = request.getParameter("nomeEvento");
        String dataEvento = request.getParameter("dataEvento");

        if (nomeEvento == null || nomeEvento.trim().isEmpty() || dataEvento == null || dataEvento.trim().isEmpty()) {
            request.setAttribute("mensagem", "Erro: Todos os campos devem ser preenchidos.");
            request.setAttribute("opcao", "confirmarEditar");
            request.setAttribute("codEvento", codEvento);
            request.setAttribute("nomeEvento", nomeEvento);
            request.setAttribute("dataEvento", dataEvento);
            encaminharParapagina(request, response);
            return;
        }

        Eventos evento = new Eventos();
        evento.setCodEvento(Integer.parseInt(codEvento));
        evento.setNomeEvento(nomeEvento);
        evento.setDataEvento(converterStringParaCalendar(dataEvento));

        eventosDao.alterar(evento);
        encaminharParapagina(request, response);
    }

    protected void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codEvento = request.getParameter("codEvento");
        String nomeEvento = request.getParameter("nomeEvento");
        String dataEvento = request.getParameter("dataEvento");

        request.setAttribute("codEvento", codEvento);
        request.setAttribute("nomeEvento", nomeEvento);
        request.setAttribute("dataEvento", dataEvento);
        request.setAttribute("mensagem", "Clique em salvar para excluir");
        request.setAttribute("opcao", "confirmarExcluir");

        encaminharParapagina(request, response);
    }

    protected void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codEvento = request.getParameter("codEvento");

        Eventos evento = new Eventos();
        evento.setCodEvento(Integer.parseInt(codEvento));

        eventosDao.excluir(evento);
        encaminharParapagina(request, response);
    }

    protected void cancelar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codEvento", "0");
        request.setAttribute("nomeEvento", "");
        request.setAttribute("dataEvento", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParapagina(request, response);
    }

    private void encaminharParapagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Eventos> listaEventos = eventosDao.buscarTodosEventos();
        request.setAttribute("listaEventos", listaEventos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroEventos.jsp");
        dispatcher.forward(request, response);
    }

    private Calendar converterStringParaCalendar(String dataStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dataStr));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
