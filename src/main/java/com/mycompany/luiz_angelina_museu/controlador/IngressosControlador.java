package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.IngressosDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.MuseusDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.VisitantesDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.VisitasDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Ingressos;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Museus;
import com.mycompany.luiz_angelina_museu.modelo.entidade.TipoIngresso;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitantes;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Visitas;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioIngressos;
// IMPORTAÇÃO DA CLASSE DO RELATÓRIO
import com.mycompany.luiz_angelina_museu.servico.ConverteData;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/IngressosControlador")
public class IngressosControlador extends HttpServlet {

    private VisitasDAO visitasDAO;
    private VisitantesDAO visitantesDAO;
    private MuseusDAO museusDAO;
    private IngressosDAO ingressosDAO;

    @Override
    public void init() throws ServletException {
        visitasDAO = new VisitasDAO();
        visitantesDAO = new VisitantesDAO();
        museusDAO = new MuseusDAO();
        ingressosDAO = new IngressosDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processarRequisicao(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processarRequisicao(request, response);
    }

    private void processarRequisicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        if (opcao == null || opcao.isEmpty()) {
            opcao = "carregarPagina";
        }

        try {
            switch (opcao) {
                case "carregarPagina":
                    carregarPagina(request, response);
                    break;
                case "iniciarVenda":
                    iniciarVenda(request, response);
                    break;
                case "adicionarCarrinho":
                    adicionarCarrinho(request, response);
                    break;
                case "removerCarrinho":
                    removerCarrinho(request, response);
                    break;
                case "finalizarVenda":
                    finalizarVenda(request, response);
                    break;
                case "gerarRelatorio":
                    GeradorRelatorioIngressos gerador = new GeradorRelatorioIngressos();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelarVenda":
                    cancelarVenda(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida.");
            }
        } catch (Exception e) {
            request.setAttribute("mensagemErro", "Erro: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void carregarPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("visitaAtiva");
        session.removeAttribute("carrinho");
        encaminharParaPagina(request, response);
    }

    private void iniciarVenda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codVisitante = Integer.parseInt(request.getParameter("codVisitante"));
        String dataVisita = request.getParameter("dataVisita");

        Visitantes visitante = visitantesDAO.buscarVisitantePorId(codVisitante);
        if (visitante == null) {
            throw new IllegalArgumentException("Visitante não encontrado.");
        }

        Visitas novaVisita = new Visitas();
        novaVisita.setVisitante(visitante);
        novaVisita.setData(new ConverteData().converteCalendario(dataVisita));

        visitasDAO.salvar(novaVisita);
        novaVisita.setCodVisita(visitasDAO.getLastId());

        HttpSession session = request.getSession();
        session.setAttribute("visitaAtiva", novaVisita);
        session.setAttribute("carrinho", new ArrayList<Ingressos>());

        encaminharParaPagina(request, response);
    }

    private void adicionarCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("visitaAtiva") == null) {
            throw new IllegalStateException("Nenhuma visita ativa. Inicie uma nova venda.");
        }

        int museuId = Integer.parseInt(request.getParameter("museuId"));
        String tipo = request.getParameter("tipo");
        double preco = Double.parseDouble(request.getParameter("preco"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));

        Museus museu = museusDAO.buscarMuseuPorId(museuId);
        if (museu == null) {
            throw new IllegalArgumentException("Museu não encontrado.");
        }

        List<Ingressos> carrinho = (List<Ingressos>) session.getAttribute("carrinho");

        for (int i = 0; i < quantidade; i++) {
            Ingressos novoIngresso = new Ingressos();
            novoIngresso.setMuseu(museu); // CORREÇÃO CRÍTICA: Seta o objeto Museu completo
            novoIngresso.setTipo(TipoIngresso.valueOf(tipo));
            novoIngresso.setPreco(preco);
            carrinho.add(novoIngresso);
        }

        encaminharParaPagina(request, response);
    }

    private void removerCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int index = Integer.parseInt(request.getParameter("index"));
        HttpSession session = request.getSession(false);
        if (session != null) {
            List<Ingressos> carrinho = (List<Ingressos>) session.getAttribute("carrinho");
            if (carrinho != null && index >= 0 && index < carrinho.size()) {
                carrinho.remove(index);
            }
        }
        encaminharParaPagina(request, response);
    }

    private void finalizarVenda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("visitaAtiva") == null) {
            throw new IllegalStateException("Nenhuma visita ativa para finalizar.");
        }

        Visitas visitaAtiva = (Visitas) session.getAttribute("visitaAtiva");
        List<Ingressos> carrinho = (List<Ingressos>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            request.setAttribute("mensagem", "O carrinho está vazio. Adicione ingressos antes de finalizar.");
            encaminharParaPagina(request, response);
            return;
        }

        for (Ingressos ingresso : carrinho) {
            ingresso.setVisita(visitaAtiva);
            ingressosDAO.salvar(ingresso);
        }

        request.setAttribute("mensagem", "Venda finalizada com sucesso!");
        cancelarVenda(request, response);
    }

    private void cancelarVenda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("visitaAtiva");
        session.removeAttribute("carrinho");
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Visitantes> listaVisitantes = visitantesDAO.buscarTodosVisitantes();
        List<Museus> listaMuseus = museusDAO.buscarTodosMuseus();

        request.setAttribute("listaVisitantes", listaVisitantes);
        request.setAttribute("listaMuseus", listaMuseus);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroIngressos.jsp");
        dispatcher.forward(request, response);
    }

}
