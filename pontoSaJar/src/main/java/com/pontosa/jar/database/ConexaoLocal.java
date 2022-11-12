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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexaoLocal {

    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/PontoSa";
    private static final String user = "root";
    private static final String pass = "@110370Cli";




    public Connection getConnection(){
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Erro na conexão:", ex);
        }
        
    }
}
