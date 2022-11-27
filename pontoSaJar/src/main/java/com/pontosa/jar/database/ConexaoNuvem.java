package com.pontosa.jar.database;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscosGroup;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.processos.ProcessosGroup;
import com.pontosa.jar.log.LogError;
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

    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String url = "jdbc:sqlserver://ponto-sa.database.windows.net:1433;database=PontoSa;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;";

    private static final String user = "PontoSa";

    private static final String pass = "Camila@01";
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public ConexaoNuvem() {
        try {
            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setDriverClassName(driver);

            dataSource.setUrl(url);

            dataSource.setUsername(user);

            dataSource.setPassword(pass);

            jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            LogError log = new LogError("ConexaoNuvem");
            log.adicionarLog(String.format("%s", e.getStackTrace()));
        }
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
