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

public class GeradorRelatorioVisitantes {

    // Busca dados para o gráfico: Top 10 visitantes por gasto total
    private JRMapCollectionDataSource getDadosGrafico() {
        String sql = "SELECT vs.nome, SUM(i.preco) AS total_gasto " +
                     "FROM visitantes vs " +
                     "JOIN visitas v ON vs.codVisitante = v.Visitantes_codVisitante " +
                     "JOIN ingressos i ON v.codVisita = i.Visitas_codVisita " +
                     "GROUP BY vs.nome " +
                     "ORDER BY total_gasto DESC " +
                     "LIMIT 10";
        List<Map<String, ?>> listaDeDados = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("NOME_VISITANTE", rs.getString("nome"));
                item.put("TOTAL_GASTO", rs.getDouble("total_gasto"));
                listaDeDados.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JRMapCollectionDataSource(listaDeDados);
    }

    public void geraRelatorio(HttpServletResponse response) throws IOException {
        try (Connection conexao = ConnectionFactory.getInstance().getConnection()) {
            InputStream relatorioStream = getClass().getResourceAsStream("/relatorio_visitantes.jrxml");

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("DADOS_GRAFICO_VISITANTES", getDadosGrafico());

            JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=relatorio_visitantes.pdf");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            exporter.exportReport();

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Erro ao gerar o relatório de visitantes.", e);
        }
    }
}