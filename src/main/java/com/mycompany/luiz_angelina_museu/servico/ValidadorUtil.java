package com.mycompany.luiz_angelina_museu.servico;

import java.util.InputMismatchException;

/**
 * Classe de utilidade para realizar validações diversas, como CPF e CNPJ.
 */
public class ValidadorUtil {

    /**
     * Valida um número de CNPJ, incluindo a verificação dos dígitos verificadores.
     * @param cnpj O CNPJ a ser validado, pode conter pontos, barras e traços.
     * @return true se o CNPJ for válido, false caso contrário.
     */
    public static boolean isCnpjValido(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // CNPJs com todos os números iguais são considerados inválidos
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Verifica o tamanho
        if (cnpj.length() != 14) {
            return false;
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

        try {
            // Cálculo do 1º Dígito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

            // Cálculo do 2º Dígito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

            // Verifica se os dígitos calculados conferem com os dígitos informados
            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}
