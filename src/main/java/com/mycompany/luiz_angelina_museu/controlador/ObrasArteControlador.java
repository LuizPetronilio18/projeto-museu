package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.ArtistaDAO;
import com.mycompany.luiz_angelina_museu.modelo.dao.ObrasArteDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Artistas;
import com.mycompany.luiz_angelina_museu.modelo.entidade.ObrasArte;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioObrasArte;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/ObrasArteControlador")
public class ObrasArteControlador extends HttpServlet {

    private ObrasArte objObrasArte;
    private ObrasArteDAO objObrasArteDao;

    private ArtistaDAO objArtistaDao;

    String codObraarte, nome, descricao, Artistas_codArtista;

    @Override
    public void init() throws ServletException {
        objArtistaDao = new ArtistaDAO();
        objObrasArte = new ObrasArte();
        objObrasArteDao = new ObrasArteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "listar";
            }
            codObraarte = request.getParameter("codObraarte");
            nome = request.getParameter("nome");
            descricao = request.getParameter("descricao");
            Artistas_codArtista = request.getParameter("Artistas_codArtista");

            switch (opcao) {
                case "cadastrar":
                    cadastrar(request, response);
                    break;
                case "listar":
                    encaminharParaPagina(request, response);
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
                case "gerarRelatorio":
                    GeradorRelatorioObrasArte gerador = new GeradorRelatorioObrasArte();
                    gerador.geraRelatorio(response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("Erro: um ou mais parâmetros não são números válidos - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.getWriter().println("Erro: Parâmetros ausentes - " + e.getMessage());
        }
    }

    protected void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        objObrasArte.setNome(nome);
        objObrasArte.setDescricao(descricao);

        // Inicializar Artistas antes de usar
        if (objObrasArte.getArtista() == null) {
            objObrasArte.setArtista(new Artistas());
        }
        objObrasArte.getArtista().setCodArtista(Integer.valueOf(Artistas_codArtista));

        objObrasArteDao.salvar(objObrasArte);
        encaminharParaPagina(request, response);
    }

    protected void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("codObraarte", codObraarte);
        request.setAttribute("nome", nome);
        request.setAttribute("descricao", descricao);
        request.setAttribute("Artistas_codArtista", Artistas_codArtista);
        request.setAttribute("mensagem", "Edite os dados e clique em salvar");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    protected void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("codObraarte", codObraarte);
        request.setAttribute("nome", nome);
        request.setAttribute("descricao", descricao);
        request.setAttribute("Artistas_codArtista", Artistas_codArtista);
        request.setAttribute("mensagem", "Clique em salvar para excluir");
        request.setAttribute("opcao", "confirmarExcluir");
        encaminharParaPagina(request, response);
    }

    protected void confirmarEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        objObrasArte.setCodObraarte(Integer.valueOf(codObraarte));
        objObrasArte.setNome(nome);
        objObrasArte.setDescricao(descricao);

        // Inicializar Artistas antes de usar
        if (objObrasArte.getArtista() == null) {
            objObrasArte.setArtista(new Artistas());
        }
        objObrasArte.getArtista().setCodArtista(Integer.valueOf(Artistas_codArtista));

        objObrasArteDao.alterar(objObrasArte);
        encaminharParaPagina(request, response);
    }

    protected void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        objObrasArte.setCodObraarte(Integer.valueOf(codObraarte));
        objObrasArteDao.excluir(objObrasArte);
        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Artistas> listaArtistas = objArtistaDao.buscarTodosArtistas();
        request.setAttribute("listaArtistas", listaArtistas);

        List<ObrasArte> listaObrasArte = objObrasArteDao.buscarTodasObras();
        request.setAttribute("listaObrasArte", listaObrasArte);

        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroObrasArte.jsp");
        enviar.forward(request, response);

    }

    protected void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("codObraarte", "0");
        request.setAttribute("nome", "");
        request.setAttribute("descricao", "");
        request.setAttribute("Artistas_codArtista", "");
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }
}
