package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.ExposicoesDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Exposicoes;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioExposicoes;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/ExposicaoControlador")
public class ExposicaoControlador extends HttpServlet {

    private Exposicoes objExposicoes;
    private ExposicoesDAO objExposicoesDao;
    private String nome, sobreExposicao, codExposicao;

    @Override
    public void init() throws ServletException {
        objExposicoesDao = new ExposicoesDAO();
        objExposicoes = new Exposicoes();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "listar";
            }

            codExposicao = request.getParameter("codExposicao");
            nome = request.getParameter("nome");
            sobreExposicao = request.getParameter("sobreExposicao");

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
                case "gerarRelatorio":
                    GeradorRelatorioExposicoes gerador = new GeradorRelatorioExposicoes();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }

        } catch (NumberFormatException e) {
            response.getWriter().print("Erro: parâmetros numéricos inválidos - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.getWriter().print("Erro: " + e.getMessage());
        }
    }

    protected void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (nome == null || nome.trim().isEmpty() || sobreExposicao == null || sobreExposicao.trim().isEmpty()) {
            request.setAttribute("mensagem", "Erro: Todos os campos devem ser preenchidos.");
            request.setAttribute("opcao", "cadastrar");
            request.setAttribute("nome", nome);
            request.setAttribute("sobreExposicao", sobreExposicao);
            encaminharParaPagina(request, response);
            return;
        }

        objExposicoes.setNome(nome);
        objExposicoes.setSobreExposicao(sobreExposicao);
        objExposicoesDao.salvar(objExposicoes);
        encaminharParaPagina(request, response);
    }

    protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codExposicao", codExposicao);
        request.setAttribute("nome", nome);
        request.setAttribute("sobreExposicao", sobreExposicao);
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    protected void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (nome == null || nome.trim().isEmpty() || sobreExposicao == null || sobreExposicao.trim().isEmpty()) {
            request.setAttribute("mensagem", "Erro: Todos os campos devem ser preenchidos.");
            request.setAttribute("opcao", "confirmarEditar");
            request.setAttribute("codExposicao", codExposicao);
            request.setAttribute("nome", nome);
            request.setAttribute("sobreExposicao", sobreExposicao);
            encaminharParaPagina(request, response);
            return;
        }

        objExposicoes.setCodExposicao(Integer.parseInt(codExposicao));
        objExposicoes.setNome(nome);
        objExposicoes.setSobreExposicao(sobreExposicao);
        objExposicoesDao.alterar(objExposicoes);
        encaminharParaPagina(request, response);
    }

    protected void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codExposicao", codExposicao);
        request.setAttribute("nome", nome);
        request.setAttribute("sobreExposicao", sobreExposicao);
        request.setAttribute("mensagem", "Clique em salvar para excluir");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParaPagina(request, response);
    }

    protected void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        objExposicoes.setCodExposicao(Integer.parseInt(codExposicao));
        objExposicoesDao.excluir(objExposicoes);
        encaminharParaPagina(request, response);
    }

    protected void cancelar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codExposicao", "0");
        request.setAttribute("nome", "");
        request.setAttribute("sobreExposicao", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Exposicoes> listaExposicoes = objExposicoesDao.buscarTodasExposicao();
        request.setAttribute("listaExposicoes", listaExposicoes);
        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroExposicoes.jsp");
        enviar.forward(request, response);
    }
}
