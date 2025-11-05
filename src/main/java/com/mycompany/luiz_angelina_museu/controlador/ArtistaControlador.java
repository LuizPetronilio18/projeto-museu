package com.mycompany.luiz_angelina_museu.controlador;

import com.mycompany.luiz_angelina_museu.modelo.dao.ArtistaDAO;
import com.mycompany.luiz_angelina_museu.modelo.entidade.Artistas;
import com.mycompany.luiz_angelina_museu.relatorios.GeradorRelatorioArtistas;
import com.mycompany.luiz_angelina_museu.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(WebConstante.BASE_PATH + "/ArtistaControlador")
public class ArtistaControlador extends HttpServlet {

    private ArtistaDAO objartistasDao;

    @Override
    public void init() throws ServletException {
        objartistasDao = new ArtistaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                throw new IllegalArgumentException("Ação não especificada.");
            }

            switch (opcao) {
                case "cadastrar":
                    cadastrar(request, response);
                    break;
                case "confirmarEditar":
                    confirmarEditar(request, response);
                    break;
                case "confirmarExcluir":
                    confirmarExcluir(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Ocorreu um erro: " + e.getMessage());
            encaminharParaPagina(request, response);
            e.printStackTrace();
        }
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
                    request.setAttribute("opcao", "cadastrar"); // Define a ação padrão do formulário
                    encaminharParaPagina(request, response);
                    break;
                case "editar":
                    editar(request, response);
                    break;
                case "excluir":
                    excluir(request, response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                case "gerarRelatorio":
                    GeradorRelatorioArtistas gerador = new GeradorRelatorioArtistas();
                    gerador.geraRelatorio(response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Ocorreu um erro: " + e.getMessage());
            encaminharParaPagina(request, response);
            e.printStackTrace();
        }
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeArtista = request.getParameter("nomeArtista");
        String nacionalidade = request.getParameter("nacionalidadae");

        String erro = validarCampos(nomeArtista, nacionalidade);
        if (erro != null) {
            request.setAttribute("erro", erro);
            request.setAttribute("opcao", "cadastrar");
            request.setAttribute("nomeArtista", nomeArtista); // Devolve o valor preenchido
            request.setAttribute("nacionalidadae", nacionalidade);
            encaminharParaPagina(request, response);
            return;
        }

        Artistas novoArtista = new Artistas();
        novoArtista.setNomeArtista(nomeArtista);
        novoArtista.setNacionalidadae(nacionalidade);
        objartistasDao.salvar(novoArtista);

        request.setAttribute("mensagem", "Artista cadastrado com sucesso!");
        cancelar(request, response); // Limpa os campos após o sucesso
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codArtistaStr = request.getParameter("codArtista");
        
        Artistas artista = objartistasDao.buscarArtistasPorId(Integer.parseInt(codArtistaStr));

        request.setAttribute("artista", artista); // Envia o objeto inteiro para o formulário
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");
        request.setAttribute("opcao", "confirmarEditar");
        encaminharParaPagina(request, response);
    }

    private void confirmarEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codArtista = request.getParameter("codArtista");
        String nomeArtista = request.getParameter("nomeArtista");
        String nacionalidade = request.getParameter("nacionalidadae");

        String erro = validarCampos(nomeArtista, nacionalidade);
        if (erro != null) {
            request.setAttribute("erro", erro);
            Artistas artista = new Artistas();
            artista.setCodArtista(Integer.parseInt(codArtista));
            artista.setNomeArtista(nomeArtista);
            artista.setNacionalidadae(nacionalidade);
            request.setAttribute("artista", artista); // Devolve os dados para o formulário
            request.setAttribute("opcao", "confirmarEditar");
            encaminharParaPagina(request, response);
            return;
        }

        Artistas artista = new Artistas();
        artista.setCodArtista(Integer.parseInt(codArtista));
        artista.setNomeArtista(nomeArtista);
        artista.setNacionalidadae(nacionalidade);
        objartistasDao.alterar(artista);
        
        request.setAttribute("mensagem", "Artista alterado com sucesso!");
        cancelar(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String codArtistaStr = request.getParameter("codArtista");
        
        Artistas artista = objartistasDao.buscarArtistasPorId(Integer.parseInt(codArtistaStr));

        request.setAttribute("artista", artista);
        request.setAttribute("mensagem", "Clique em 'Salvar' para confirmar a exclusão.");
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("readOnly", true); // Atributo para desabilitar campos no JSP
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codArtista = request.getParameter("codArtista");
        
        Artistas artista = new Artistas();
        artista.setCodArtista(Integer.parseInt(codArtista));
        objartistasDao.excluir(artista);
        
        request.setAttribute("mensagem", "Artista excluído com sucesso!");
        cancelar(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Artistas> listaArtista = objartistasDao.buscarTodosArtistas();
        request.setAttribute("listaArtistas", listaArtista);
        RequestDispatcher enviar = request.getRequestDispatcher("/CadastroArtista.jsp");
        enviar.forward(request, response);
    }
    
    private void cancelar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("opcao", "cadastrar");
        encaminharParaPagina(request, response);
    }

    private String validarCampos(String nomeArtista, String nacionalidade) {
        if (nomeArtista == null || nomeArtista.trim().isEmpty()) {
            return "O campo nome do artista é obrigatório.";
        }
        if (nacionalidade == null || nacionalidade.trim().isEmpty()) {
            return "O campo nacionalidade é obrigatório.";
        }
        return null;
    }
}