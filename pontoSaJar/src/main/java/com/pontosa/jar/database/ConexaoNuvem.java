package com.pontosa.jar.database;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscosGroup;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.processos.ProcessosGroup;
import com.pontosa.jar.slack.Slack;
import com.pontosa.jar.usuario.Dispositivo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoNuvem {

    private JdbcTemplate jdbcTemplate;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public ConexaoNuvem() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
// exemplo para MySql: "com.mysql.cj.jdbc.Driver"
        dataSource.setUrl("jdbc:sqlserver://ponto-sa.database.windows.net:1433;database=PontoSa;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;");
// exemplo para MySql: "jdbc:mysql://localhost:3306/meubanco"
        dataSource.setUsername("PontoSa");
        dataSource.setPassword("Camila@01");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int[] salvarEmLote(List<Double> dispositivos, Integer dispositivo, List<Integer> metricas) {
        this.jdbcTemplate.batchUpdate("INSERT INTO historico(fk_dispositivo, fk_tipo_metrica ,registro, data_hora) VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
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
    

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
