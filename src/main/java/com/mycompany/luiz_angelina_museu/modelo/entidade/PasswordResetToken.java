/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.luiz_angelina_museu.modelo.entidade;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author luizo
 */
public class PasswordResetToken implements Serializable {

    private static final int EXPIRATION = 60 * 24; // 24 horas

    private int id;
    private String token;
    private int usuarioId;
    // --- CORREÇÃO APLICADA AQUI ---
    private Timestamp dataExpiracao;

    public PasswordResetToken() {
        this.dataExpiracao = calculateExpiryDate(EXPIRATION);
    }

    private Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    // --- CORREÇÃO APLICADA AQUI ---
    public Timestamp getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Timestamp dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}