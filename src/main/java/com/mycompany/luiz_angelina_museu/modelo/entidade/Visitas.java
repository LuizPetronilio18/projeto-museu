package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Entidade que representa uma Visita.
 * A propriedade 'data' foi corrigida para usar Calendar em vez de String.
 */
public class Visitas implements Serializable {

    private int codVisita;
    private Visitantes visitante;
    private Calendar data; // CORRIGIDO: Alterado de String para Calendar

    // Getters e Setters
    public int getCodVisita() {
        return codVisita;
    }

    public void setCodVisita(int codVisita) {
        this.codVisita = codVisita;
    }

    public Visitantes getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitantes visitante) {
        this.visitante = visitante;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }
}
