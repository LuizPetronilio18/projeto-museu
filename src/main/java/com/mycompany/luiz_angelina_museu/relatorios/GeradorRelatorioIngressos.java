package com.mycompany.luiz_angelina_museu.relatorios;

import com.mycompany.luiz_angelina_museu.modelo.dao.ConnectionFactory;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class GeradorRelatorioIngressos {

    // Busca dados para o gráfico de barras: receita por museu
    private JRMapCollectionDataSource getDadosGraficoReceita() {
        String sql = "SELECT m.nome, SUM(i.preco) AS receita_total " +
                     "FROM ingressos i " +
                     "JOIN museus m ON i.Museus_codMuseu = m.codMuseu " +
                     "GROUP BY m.nome " +
                     "ORDER BY receita_total DESC";
        List<Map<String, ?>> listaDeDados = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("MUSEU_NOME", rs.getString("nome"));
                item.put("RECEITA_TOTAL", rs.getDouble("receita_total"));
                listaDeDados.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JRMapCollectionDataSource(listaDeDados);
    }

    // Busca dados para o gráfico de pizza: contagem por tipo de ingresso
    private JRMapCollectionDataSource getDadosGraficoTipo() {
        String sql = "SELECT tipo, COUNT(codIngresso) AS total_tipo FROM ingressos GROUP BY tipo";
        List<Map<String, ?>> listaDeDados = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("TIPO_INGRESSO", rs.getString("tipo"));
                item.put("TOTAL_TIPO", rs.getInt("total_tipo"));
                listaDeDados.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JRMapCollectionDataSource(listaDeDados);
    }

    public void geraRelatorio(HttpServletResponse response) throws IOException {
        try (Connection conexao = ConnectionFactory.getInstance().getConnection()) {
            InputStream relatorioStream = getClass().getResourceAsStream("/relatorio_ingressos.jrxml");

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("DADOS_GRAFICO_RECEITA", getDadosGraficoReceita());
            parametros.put("DADOS_GRAFICO_TIPO", getDadosGraficoTipo());

            JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=relatorio_ingressos.pdf");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            exporter.exportReport();

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Erro ao gerar o relatório de ingressos.", e);
        }
    }
}