package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.io.Serializable;

/**
 * Entidade Ingressos atualizada para usar o Enum TipoIngresso.
 */
public class Ingressos implements Serializable {

    private int codIngresso;
    private TipoIngresso tipo;
    private double preco;
    private Visitas visita;
    private Museus museu;

    public Ingressos() {
    }

    public int getCodIngresso() {
        return codIngresso;
    }

    public void setCodIngresso(int codIngresso) {
        this.codIngresso = codIngresso;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public void setTipo(TipoIngresso tipo) {
        this.tipo = tipo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Visitas getVisita() {
        return visita;
    }

    public void setVisita(Visitas visita) {
        this.visita = visita;
    }

    public Museus getMuseu() {
        return museu;
    }

    public void setMuseu(Museus museu) {
        this.museu = museu;
    }
}
