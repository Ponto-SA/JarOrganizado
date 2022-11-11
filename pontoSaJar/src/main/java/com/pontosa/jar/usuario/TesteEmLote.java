package com.pontosa.jar.usuario;

import com.pontosa.jar.database.ConexaoNuvem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TesteEmLote {

    ConexaoNuvem conexaoNuvem = new ConexaoNuvem();

    Dispositivo dispositivo = new Dispositivo();

//    public int[] salvarEmLote(List<String> dispositivos) {
//        conexaoNuvem.getConnectionTemplate().batchUpdate("INSERT INTO DISPOSITIVOS VALUE (?, ?)", new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
//                preparedStatement.setString(1, dispositivos.get(i).getHostName());
//                preparedStatement.setString(2, dispositivos.get(i).getHostAddress());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return dispositivos.size();
//            }
//        });
//    }
}
