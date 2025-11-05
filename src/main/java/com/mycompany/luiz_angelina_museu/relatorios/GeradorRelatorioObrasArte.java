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

public class GeradorRelatorioObrasArte {

    private JRMapCollectionDataSource getDadosGrafico() {
        String sql = "SELECT a.nomeArtista, COUNT(o.codObraarte) AS total_obras " +
                     "FROM obrasarte o " +
                     "JOIN artistas a ON o.Artistas_codArtista = a.codArtista " +
                     "GROUP BY a.nomeArtista " +
                     "ORDER BY total_obras DESC";
        List<Map<String, ?>> listaDeDados = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("NOME_ARTISTA", rs.getString("nomeArtista"));
                item.put("TOTAL_OBRAS", rs.getInt("total_obras"));
                listaDeDados.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JRMapCollectionDataSource(listaDeDados);
    }

    public void geraRelatorio(HttpServletResponse response) throws IOException {
        try (Connection conexao = ConnectionFactory.getInstance().getConnection()) {
            InputStream relatorioStream = getClass().getResourceAsStream("/relatorio_obrasarte.jrxml");

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("DADOS_GRAFICO_OBRAS", getDadosGrafico());

            JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=relatorio_obrasdearte.pdf");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            exporter.exportReport();

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Erro ao gerar o relatório de obras de arte.", e);
        }
    }
}