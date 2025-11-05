package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.MuseusDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Museus;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioMuseus;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/MuseusControlador")
public class MuseusControlador extends HttpServlet {

    private MuseusDAO museusDAO;

    @Override
    public void init() throws ServletException {
        museusDAO = new MuseusDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        if (opcao == null || opcao.isEmpty()) {
            return;
        }

        switch (opcao) {
            case "cadastrar":
                salvar(request, response, true);
                break;
            case "confirmarEditar":
                salvar(request, response, false);
                break;
            case "confirmarExcluir":
                confirmarExcluir(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        if (opcao == null || opcao.isEmpty()) {
            opcao = "listar";
        }

        switch (opcao) {
            case "listar":
                listar(request, response);
                break;
            case "editar":
                editar(request, response);
                break;
            case "excluir":
                excluir(request, response);
                break;
            case "cancelar":
                listar(request, response);
                break;
            case "gerarRelatorio":
                 GeradorRelatorioMuseus gerador = new GeradorRelatorioMuseus();
                 gerador.geraRelatorio(response);
                 break;
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("opcao", "cadastrar");
        List<Museus> listaMuseus = museusDAO.buscarTodosMuseus();
        request.setAttribute("listaMuseus", listaMuseus);
        encaminharParaPagina(request, response);
    }

    private void salvar(HttpServletRequest request, HttpServletResponse response, boolean isNovo) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String localizacao = request.getParameter("localizacao");
        String horario = request.getParameter("horariofuncionamento");
        String descricao = request.getParameter("descricao");
        String cnpj = request.getParameter("cnpj");

        Museus museu = new Museus();
        museu.setNome(nome);
        museu.setLocalizacao(localizacao);
        museu.setHorariofuncionamento(horario);
        museu.setDescricao(descricao);
        museu.setCnpj(cnpj);

        if (isNovo) {
            museusDAO.salvar(museu);
            request.setAttribute("mensagem", "Museu cadastrado com sucesso!");
        } else {
            String codMuseuStr = request.getParameter("codMuseu");
            museu.setCodMuseu(Integer.parseInt(codMuseuStr));
            museusDAO.alterar(museu);
            request.setAttribute("mensagem", "Museu alterado com sucesso!");
        }

        listar(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codMuseuStr = request.getParameter("codMuseu");
        Museus museu = museusDAO.buscarMuseuPorId(Integer.parseInt(codMuseuStr));
        
        request.setAttribute("museu", museu);
        request.setAttribute("opcao", "confirmarEditar");
        listar(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codMuseuStr = request.getParameter("codMuseu");
        Museus museu = museusDAO.buscarMuseuPorId(Integer.parseInt(codMuseuStr));
        
        request.setAttribute("museu", museu);
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("readOnly", true);
        listar(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codMuseuStr = request.getParameter("codMuseu");
        Museus museu = new Museus();
        museu.setCodMuseu(Integer.parseInt(codMuseuStr));
        
        museusDAO.excluir(museu);
        request.setAttribute("mensagem", "Museu excluído com sucesso!");
        
        listar(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroMuseus.jsp");
        dispatcher.forward(request, response);
    }
}