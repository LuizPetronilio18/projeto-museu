package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Doacoes {
    
    private Integer codDoacao;
    private Double valorDoacao;
    private String nomeDoacao;
    private Calendar dataDoacao;
    
    // Renomeado o atributo para 'museu'
    private Museus museu = new Museus();

    public Integer getCodDoacao() {
        return codDoacao;
    }

    public void setCodDoacao(Integer codDoacao) {
        this.codDoacao = codDoacao;
    }

    public Double getValorDoacao() {
        return valorDoacao;
    }

    public void setValorDoacao(Double valorDoacao) {
        this.valorDoacao = valorDoacao;
    }

    public String getNomeDoacao() {
        return nomeDoacao;
    }

    public void setNomeDoacao(String nomeDoacao) {
        this.nomeDoacao = nomeDoacao;
    }

    public Calendar getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(Calendar dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public Museus getMuseu() {
        return museu;
    }

    public void setMuseu(Museus museu) {
        this.museu = museu;
    }
    
    public String getDataDoacaoFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataDoacao.getTime());
    }
}
