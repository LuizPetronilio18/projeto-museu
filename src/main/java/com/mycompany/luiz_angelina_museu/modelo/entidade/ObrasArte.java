package com.mycompany.luiz_angelina_museu.modelo.entidade;

public class ObrasArte {

    private Integer codObraarte;
    private String nome;
    private String descricao;

    private Artistas artista = new Artistas(); // Nome simplificado e claro

    public Integer getCodObraarte() {
        return codObraarte;
    }

    public void setCodObraarte(Integer codObraarte) {
        this.codObraarte = codObraarte;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Artistas getArtista() {
        return artista;
    }

    public void setArtista(Artistas artista) {
        this.artista = artista;
    }
}
