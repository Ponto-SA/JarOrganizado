package com.pontosa.jar.usuario;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscosGroup;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.processos.ProcessosGroup;
import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.slack.Slack;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dispositivo {
    
    private Looca looca = new Looca();
    private LocalDateTime dt = LocalDateTime.now();
    private DiscosGroup disco = new DiscosGroup();
    private ProcessosGroup processos = new ProcessosGroup();
    private List<Disco> listaDisco = new ArrayList<>();
    private List<Volume> listaVolume = new ArrayList<>();
    private ConexaoLocal conexao = new ConexaoLocal();
    private Slack slack = new Slack();

    private String hostName;

    private String hostAddress;

    public Dispositivo() {
        this.hostName = hostName;
        this.hostAddress = hostAddress;
    }

    
    public void loopRegistro() throws InterruptedException {
     
        Integer idDispositivo = 0;
         
        try {
            String acharId = String.format("SELECT usuario.id FROM usuario_maquina inner join usuario on usuario.id = usuario_maquina.fk_usuario inner join dispositivo on dispositivo.id = usuario_maquina.fk_dispositivo where dispositivo.host_name = '%s'", getHostName());

            Statement st2 = conexao.getConnection().createStatement();

            ResultSet rs = st2.executeQuery(acharId);

            while (rs.next()) {
            idDispositivo = rs.getInt("id");
            }


            System.out.println(idDispositivo);

            st2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }        
                System.out.println("Entrando no loop");
                memoria(idDispositivo);
                System.out.println("Inseriu registro memoria");
                processador(idDispositivo);
                System.out.println("Inseriu registro processador");
                temperatura(idDispositivo);
                System.out.println("Inseriu registro temperatura");
                disco(idDispositivo);
                System.out.println("Inseriu Disco");
    }

    
    public void especificacao () {
         Integer idDispositivo = 0;
        
        try {
            String acharId = String.format("SELECT usuario.id FROM usuario_maquina inner join usuario on usuario.id = usuario_maquina.fk_usuario inner join dispositivo on dispositivo.id = usuario_maquina.fk_dispositivo where dispositivo.host_name = '%s'", getHostName());

            Statement st2 = conexao.getConnection().createStatement();

            ResultSet rs = st2.executeQuery(acharId);

            while (rs.next()) {
            idDispositivo = rs.getInt("id");
            }


            System.out.println(idDispositivo);

            st2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            System.out.println("Teste especificação");
            Double memoriaTotal = Double.valueOf((looca.getMemoria().getTotal() / 1024)/ 1024) / 1024;
            Locale.setDefault(Locale.US);
            System.out.println(memoriaTotal);
            String sql = (String.format("UPDATE `dispositivo` SET tipo_processador = '%s', memoria_total = '%.2f' WHERE host_name = '%s'",
                    looca.getProcessador().getNome(), memoriaTotal, getHostName()));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }        
         try{
             Locale.setDefault(Locale.US);
               if (disco.getDiscos().size() > 1){
            for (int i = 0; i < disco.getQuantidadeDeDiscos(); i++){
                    Locale.setDefault(Locale.US);
                listaDisco.add(disco.getDiscos().get(i));
                listaVolume.add(disco.getVolumes().get(i));
              String modeloDisco =  listaDisco.get(i).getModelo().substring(0, listaDisco.get(i).getModelo().indexOf("("));
               String serialDisco = listaDisco.get(i).getSerial();
              Double tamanhoDisco = (double)  listaDisco.get(i).getTamanho();
                
                System.out.println("Teste Disco");
            String sql = (String.format("INSERT INTO disco VALUES (null, '%s', '%s', '%.1f', '%d')",
                   modeloDisco , serialDisco, tamanhoDisco, idDispositivo));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
                
            }
        } else {
                Locale.setDefault(Locale.US);
        System.out.println(disco.getDiscos());
            System.out.println(disco.getDiscos().size());
         String modelo =  disco.getDiscos().get(0).getModelo().substring(0, disco.getDiscos().get(0).getModelo().indexOf("("));
          String serial =  disco.getDiscos().get(0).getSerial();
        Long tamanho =  (disco.getDiscos().get(0).getTamanho());
                   System.out.println(tamanho);
            listaVolume.add(disco.getVolumes().get(0));
            listaVolume.get(0).getDisponivel();
                   System.out.println(idDispositivo);
                System.out.println("Teste Disco");
            String sql = (String.format("INSERT INTO disco VALUES (null, '%s', '%s', %.0f, '%d')",
                   modelo , serial, Double.valueOf(String.valueOf(tamanho).substring(0, 3).toString()), idDispositivo));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
        }   
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public void processos() {
        //yohan yucatan
    }

    public void memoria(Integer idDispositivo) {
        try{
            System.out.println("Teste Memoria");
            Double memoriaUso = Double.longBitsToDouble(looca.getMemoria().getEmUso());
            Double memoriaTotal = Double.longBitsToDouble(looca.getMemoria().getTotal());
            Double memoriaUsoPorc = (memoriaUso / memoriaTotal) * 100;
            if (memoriaUsoPorc > 40){
                Slack.mensagemSlack(String.format("Novo teste uso de memoria chegou em %.2f", memoriaUsoPorc));
            }
            Locale.setDefault(Locale.US);
            String sql = (String.format("INSERT INTO `historico` (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '2', '%.1f')", idDispositivo, memoriaUsoPorc));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disco(Integer idDispositivo) {
        
       Locale.setDefault(Locale.US);
        
       Double tamanhoTotal;
       Double tamanhoDisponivel;
       Double emUso;
       Double discoUsoPorc;
            
       try{
          
               if (disco.getDiscos().size() > 1){
            for (int i = 0; i < disco.getDiscos().size(); i++){
              tamanhoTotal = (double) disco.getDiscos().get(i).getTamanho();
       tamanhoDisponivel = (double) disco.getVolumes().get(i).getDisponivel();
       emUso = tamanhoTotal - tamanhoDisponivel;
       discoUsoPorc = (emUso / tamanhoTotal) * 100;
            String sql = (String.format("INSERT INTO `historico` (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '3', '%.1f')", idDispositivo, discoUsoPorc));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
            }
        } else {
               tamanhoTotal = (double) disco.getDiscos().get(0).getTamanho();
       tamanhoDisponivel = (double) disco.getVolumes().get(0).getDisponivel();
       emUso = tamanhoTotal - tamanhoDisponivel;
       discoUsoPorc = (emUso / tamanhoTotal) * 100;
                   
                String sql = (String.format("INSERT INTO `historico` (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '3', '%.1f')", idDispositivo, discoUsoPorc));
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);   
               }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processador(Integer idDispositivo){
        try{
            
            String sql = (String.format("INSERT INTO `historico` (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '1', '%.1f')", idDispositivo, looca.getProcessador().getUso()));
            Locale.setDefault(Locale.US);
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void temperatura(Integer idDispositivo) {
        try{
            System.out.println("Teste especificação");
            String sql = (String.format("INSERT INTO `historico` (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '4', %.1f)", idDispositivo, looca.getTemperatura().getTemperatura()));
            Locale.setDefault(Locale.US);
            PreparedStatement pstmt = conexao.getConnection().prepareStatement(sql);
            pstmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Boolean login(String email, String senha){
        
        int id = 0;
        try {
            
            System.out.println("Estou buscando...");
            String sql = (String.format("SELECT * FROM usuario where email = '%s' and senha = '%s'", email, senha));
            //PreparedStatement pstmt = getConnection().prepareStatement(sql);
            Statement st = conexao.getConnection().createStatement();

            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()){
                id = rs.getInt("id");
                String nome = rs.getString("nome");
                String emailResultado = rs.getString("email");
                String senhaResultado = rs.getString("senha");
                
                System.out.println(String.format("%s, %s, %s, %s", id, nome, emailResultado, senhaResultado));
            }
            st.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        if (id > 0){
            return true;
        } else {
        return false;
        }
           
    }
    
    public String getHostName() {
        try {
            return hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHostAddress() {
        try {
            return hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}