/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pontosa.jar.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ythudson
 */

public class ConexaoNuvem {
    
    private JdbcTemplate connection;

    private static final String driver = "com.mysql.cj.jdbc.Driver";

    private static final String url = "";

    private static final String user = "";

    private static final String pass = "";

    public ConexaoNuvem() {

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUrl("jdbc:mysql://localhost:3306/cybervision");

        dataSource.setUsername("root");

        dataSource.setPassword("urubu100");

        this.connection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConnectionTemplate() {
        return connection;
    }
}
