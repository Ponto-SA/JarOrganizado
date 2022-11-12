package com.pontosa.jar.usuario;

import com.pontosa.jar.database.ConexaoNuvem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TesteEmLote {

    ConexaoNuvem conexaoNuvem = new ConexaoNuvem();

    Dispositivo dispositivo = new Dispositivo();

    public int[] salvarEmLote(List<Double> dispositivos, Integer dispositivo, List<Integer> metricas) {
        conexaoNuvem.getConnectionTemplate().batchUpdate("INSERT INTO historico(fk_dispositivo, fk_tipo_metrica ,registro, data_hora) VALUES (?, ?, ?, default)", new BatchPreparedStatementSetter() {
           @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, dispositivo);
                preparedStatement.setInt(2, metricas.get(i));
                preparedStatement.setDouble(3, dispositivos.get(i));
                      
           }

           @Override
           public int getBatchSize() {
                return dispositivos.size();
           }
       });
        return null;
    }
    
}
