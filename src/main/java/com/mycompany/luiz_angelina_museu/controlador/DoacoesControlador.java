package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.DoacoesDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.MuseusDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Doacoes;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Museus;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioDoacoes;
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

@WebServlet(WebConstante.BASE_PATH + "/DoacoesControlador")
public class DoacoesControlador extends HttpServlet {

    private DoacoesDAO objDoacoesDao;
    private MuseusDAO objMuseusDao;
    private final ConverteData converte = new ConverteData();

    @Override
    public void init() throws ServletException {
        objDoacoesDao = new DoacoesDAO();
        objMuseusDao = new MuseusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "listar";
            }

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
                case "excluir":
                    excluir(request, response);
                    break;
                case "confirmarEditar":
                    confirmarEditar(request, response);
                    break;
                case "confirmarExcluir":
                    confirmarExcluir(request, response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                case "gerarRelatorio":
                    GeradorRelatorioDoacoes gerador = new GeradorRelatorioDoacoes();
                    gerador.geraRelatorio(response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            response.getWriter().println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Doacoes objDoacoes = preencherObjeto(request);
        objDoacoesDao.salvar(objDoacoes);
        encaminharParaPagina(request, response);
    }

    protected void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAtributosNaTela(request);
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    protected void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAtributosNaTela(request);
        request.setAttribute("mensagem", "Clique em salvar para excluir");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParaPagina(request, response);
    }

    protected void confirmarEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Doacoes objDoacoes = preencherObjeto(request);
        objDoacoes.setCodDoacao(Integer.parseInt(request.getParameter("codDoacao")));
        objDoacoesDao.alterar(objDoacoes);
        encaminharParaPagina(request, response);
    }

    protected void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int codDoacao = Integer.parseInt(request.getParameter("codDoacao"));
        Doacoes objDoacoes = new Doacoes();
        objDoacoes.setCodDoacao(codDoacao);
        objDoacoesDao.excluir(objDoacoes);
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Museus> listaMuseu = objMuseusDao.buscarTodosMuseus();
        request.setAttribute("listaMuseu", listaMuseu);

        List<Doacoes> listaDoacao = objDoacoesDao.buscarTodasDoacoes();
        request.setAttribute("listaDoacao", listaDoacao);

        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroDoacoes.jsp");
        enviar.forward(request, response);
    }

    protected void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("codDoacao", "0");
        request.setAttribute("nomeDoacao", "");
        request.setAttribute("valorDoacao", "");
        // Alterei para "museu_codMuseu" para ficar igual ao que o JSP espera
        request.setAttribute("museu_codMuseu", "");
        request.setAttribute("dataDoacao", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private Doacoes preencherObjeto(HttpServletRequest request) {
        Doacoes obj = new Doacoes();
        obj.setNomeDoacao(request.getParameter("nomeDoacao"));
        obj.setValorDoacao(Double.valueOf(request.getParameter("valorDoacao")));
        obj.setDataDoacao(converte.converteCalendario(request.getParameter("dataDoacao")));
        Museus museu = new Museus();
        museu.setCodMuseu(Integer.valueOf(request.getParameter("museu_codMuseu"))); // igual aqui
        obj.setMuseu(museu);  // corrigido: setMuseu, não setMuseus_codMuseu
        return obj;
    }

    private void setAtributosNaTela(HttpServletRequest request) {
        request.setAttribute("codDoacao", request.getParameter("codDoacao"));
        request.setAttribute("nomeDoacao", request.getParameter("nomeDoacao"));
        request.setAttribute("valorDoacao", request.getParameter("valorDoacao"));
        request.setAttribute("dataDoacao", ConverteData.convertDateFormat(request.getParameter("dataDoacao")));
        // Também aqui ajustar para "museu_codMuseu"
        request.setAttribute("museu_codMuseu", request.getParameter("museu_codMuseu"));
    }
}
