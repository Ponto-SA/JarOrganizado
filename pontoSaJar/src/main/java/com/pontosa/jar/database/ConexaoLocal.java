/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pontosa.jar.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ythudson
 */

public class ConexaoLocal {

    private JdbcTemplate connection;

    private static final String driver = "com.mysql.cj.jdbc.Driver";

    private static final String url = "jdbc:mysql://localhost:3306/PontoSa";

    private static final String user = "root";

    private static final String pass = "Camila@01";

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public ConexaoLocal() {

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driver);

        dataSource.setUrl(url);

        dataSource.setUsername(user);

        dataSource.setPassword(pass);

        this.connection = new JdbcTemplate(dataSource);
    }

    public int[] salvarEmLote(List<Double> dispositivos, Integer dispositivo, List<Integer> metricas) {
        this.getConnectionTemplate().batchUpdate("INSERT INTO historico(fk_dispositivo, fk_tipo_metrica ,registro, data_hora) VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, dispositivo);
                preparedStatement.setInt(2, metricas.get(i));
                preparedStatement.setDouble(3, dispositivos.get(i));
                preparedStatement.setString(4, dtf.format(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return dispositivos.size();
            }
        });
        return null;
    }


    public JdbcTemplate getConnectionTemplate() {
        return connection;
    }
}
