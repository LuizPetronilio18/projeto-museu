package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.util.Calendar;
import java.util.Date;

/**
 * Entidade que representa um Evento.
 * Esta versão foi corrigida para remover campos inconsistentes com o banco de dados.
 * @author 16183566667
 */
public class Eventos {
    private int codEvento;
    private String nomeEvento;
    private Calendar dataEvento;

    public int getCodEvento() {
        return codEvento;
    }

    public void setCodEvento(int codEvento) {
        this.codEvento = codEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public Calendar getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Calendar dataEvento) {
        this.dataEvento = dataEvento;
    }

    /**
     * Método auxiliar para retornar a data como java.util.Date,
     * útil para compatibilidade com componentes de UI.
     * @return A data do evento como java.util.Date, ou null se não estiver definida.
     */
    public Date getDataEventoDate() {
        return dataEvento != null ? dataEvento.getTime() : null;
    }
}
