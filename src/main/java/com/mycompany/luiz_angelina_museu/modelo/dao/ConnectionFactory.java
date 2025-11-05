package com.mycompany.luiz_angelina_museu.modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author 16183566667
 * //Padrão Singleton
 */
public class ConnectionFactory {

    // 1. Apagamos os valores fixos (localhost, 1234)
    //    e criamos variáveis para os nomes que o Render vai nos dar
    private static final String ENV_DB_URL = "DB_URL";
    private static final String ENV_DB_USER = "DB_USER";
    private static final String ENV_DB_PASS = "DB_PASS";
    
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Variável estática que mantém a instância única de ConnectionFactory.
    private static ConnectionFactory instance;

    // O construtor é privado para impedir a criação direta de instâncias da classe fora dela mesma.
    private ConnectionFactory() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do banco de dados não encontrado", e);
        }
    }

    // Método público estático que permite o acesso à instância única da ConnectionFactory.
    // Padrão Singleton: garante que apenas uma instância seja usada em toda a aplicação.
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // 2. Modificamos este método para ler as "Variáveis de Ambiente"
    // Método para obter a conexão com o banco de dados.
    public Connection getConnection() throws SQLException {
        
        // System.getenv() é o comando Java para ler as variáveis
        // que o Render vai nos dar
        String dbUrl = System.getenv(ENV_DB_URL);
        String dbUser = System.getenv(ENV_DB_USER);
        String dbPass = System.getenv(ENV_DB_PASS);

        // Se ele não achar as variáveis, ele vai lançar um erro claro
        // Isso ajuda a saber se esquecemos de configurar o Render
        if (dbUrl == null || dbUser == null || dbPass == null) {
            throw new SQLException("Variáveis de ambiente do banco de dados (DB_URL, DB_USER, DB_PASS) não foram definidas.");
        }

        // Conecta usando as variáveis da nuvem
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
    
    // **NOVO MÉTODO ESTÁTICO**
    // Este método facilita a obtenção da conexão em outras partes do código.
    public static Connection getConexao() throws SQLException {
        return getInstance().getConnection();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        Connection con = getConnection();
        return con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }
}