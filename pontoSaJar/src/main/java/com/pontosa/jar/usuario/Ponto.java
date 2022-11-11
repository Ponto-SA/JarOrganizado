package com.pontosa.jar.usuario;

import com.pontosa.jar.database.ConexaoLocal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;


public class Ponto {

    ConexaoLocal conexao = new ConexaoLocal();

    Dispositivo hostName = new Dispositivo();


    public void Entrada() {
        try {
            Integer id = 0;
        System.out.println("Estou buscando...");
        String sql = (String.format("select usuario.id from usuario join usuario_maquina on usuario.id = usuario_maquina.FK_usuario join dispositivo on dispositivo.id = usuario_maquina.FK_dispositivo where NumeroSerie = '%d'", hostName.getHostName()));
        Statement st = conexao.getConnection().createStatement();

        ResultSet rs = st.executeQuery(sql);

            System.out.println(rs);

        if (id > 0) {
            DateTimeFormatter dataEntrada = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");

            String sll = (String.format("INSERT INTO ponto (entrada, FK_usuario) VALUES ()", dataEntrada, id));
            ResultSet rs2 = st.executeQuery(sll);

            st.close();
        }else {
            System.out.println("Não foi possivel bater o ponto");
            System.out.println(id);
        }

    } catch (Exception e){
        e.printStackTrace();
    }
    }

}
