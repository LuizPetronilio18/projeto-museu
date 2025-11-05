/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author 16183566667
 */
public class Funcionario {
    private Integer codFuncionario;
    private String nome;
    private String cpf;
    
    private Cargo cargo_codCargo = new Cargo();

    public Integer getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(Integer codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Cargo getCargo_codCargo() {
        return cargo_codCargo;
    }

    public void setCargo_codCargo(Cargo cargo_codCargo) {
        this.cargo_codCargo = cargo_codCargo;
    }


    
}
