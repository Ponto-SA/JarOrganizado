package com.pontosa.jar.usuario;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscosGroup;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.processos.ProcessosGroup;
import com.pontosa.jar.database.ConexaoNuvem;
import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.slack.Slack;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import com.github.britooo.looca.api.group.processos.Processo;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;

public class Dispositivo {

    private Looca looca = new Looca();
    private LocalDateTime dt = LocalDateTime.now();
    private DiscosGroup disco = new DiscosGroup();
    private ProcessosGroup processos = new ProcessosGroup();
    private List<Disco> listaDisco = new ArrayList<>();
    private List<Volume> listaVolume = new ArrayList<>();
    private ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
    private ConexaoLocal conexaoLocal = new ConexaoLocal();
    private Slack slack = new Slack();
    private Usuario usuario = new Usuario();

    private String hostName;

    private String hostAddress;

    public Dispositivo() {
        this.hostName = hostName;
        this.hostAddress = hostAddress;
    }

    public Map<String, Object> recuperarDispositivoId() {
        try {
            Map<String, Object> registro = conexaoNuvem.getJdbcTemplate().queryForMap(
                    "select id from dispositivo where host_name = ?", getHostName());
          

            return registro;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void loopRegistro() throws InterruptedException {

       Map<String, Object> dispositivoMomento = recuperarDispositivoId();
       
       Integer idDispositivo = Integer.valueOf(String.valueOf(dispositivoMomento.get("id")));
       
        List<Double> registros = new ArrayList<>();
        List<Integer> metricas = new ArrayList<>();

        registros.add(memoria());
        metricas.add(2);
        registros.add(processador());
        metricas.add(1);
        registros.add(disco());
        metricas.add(3);
        registros.add(memoriaProcessos());
        metricas.add(5);
        conexaoLocal.salvarEmLote(registros, idDispositivo, metricas);
        conexaoNuvem.salvarEmLote(registros, idDispositivo, metricas);
        System.out.println("Entrando no loop");
        // memoria(idDispositivo);
        System.out.println("Inseriu registro memoria");
        //processador(idDispositivo);
        System.out.println("Inseriu registro processador");
        // temperatura(idDispositivo);
        System.out.println("Inseriu registro temperatura");
        // disco(idDispositivo);
        System.out.println("Inseriu Disco");
    }

    public Double memoriaProcessos() {
        List<Processo> list = looca.getGrupoDeProcessos().getProcessos();
        Double usoMemoria = 0.0;
        for (int i = 0; i < list.size(); i++) {
            usoMemoria += list.get(i).getUsoMemoria();
        }
        return usoMemoria;
    }

    public void especificacao() {
        
    Map<String, Object> dispositivoMomento = recuperarDispositivoId();
       
       Integer idDispositivo = Integer.valueOf(String.valueOf(dispositivoMomento.get("id")));
      
        
        System.out.println("Teste especificação");
        Double memoriaTotal = Double.valueOf((looca.getMemoria().getTotal() / 1024) / 1024) / 1024;
        Locale.setDefault(Locale.US);
        System.out.println(memoriaTotal);
        String sql = (String.format("UPDATE dispositivo SET tipo_processador = '%s', memoria_total = '%.2f', sistema_operacional = '%s' WHERE host_name = '%s'",
                looca.getProcessador().getNome(), memoriaTotal, looca.getSistema().getSistemaOperacional() ,getHostName()));
        conexaoNuvem.getJdbcTemplate().update(sql);
        conexaoLocal.getConnectionTemplate().update(sql);

        if (disco.getDiscos().size() > 1) {
            for (int i = 0; i < disco.getQuantidadeDeDiscos(); i++) {
                Locale.setDefault(Locale.US);
                listaDisco.add(disco.getDiscos().get(i));
                listaVolume.add(disco.getVolumes().get(i));
                String modeloDisco = listaDisco.get(i).getModelo().substring(0, listaDisco.get(i).getModelo().indexOf("("));
                String serialDisco = listaDisco.get(i).getSerial();
                Double tamanhoDisco = (double) listaDisco.get(i).getTamanho();

                System.out.println("Teste Disco");
                String sql2 = (String.format("INSERT INTO disco (modelo, numero_serial, tamanho, fk_dispositivo) VALUES ('%s', '%s', %.0f, %d)",
                        modeloDisco, serialDisco, tamanhoDisco, idDispositivo));
                conexaoNuvem.getJdbcTemplate().update(sql2);
                conexaoLocal.getConnectionTemplate().update(sql2);

            }
        } else {
            Locale.setDefault(Locale.US);
            System.out.println(disco.getDiscos());
            System.out.println(disco.getDiscos().size());
            String modelo = disco.getDiscos().get(0).getModelo().substring(0, disco.getDiscos().get(0).getModelo().indexOf("("));
            String serial = disco.getDiscos().get(0).getSerial();
            Long tamanho = (disco.getDiscos().get(0).getTamanho());
            System.out.println(tamanho);
            listaVolume.add(disco.getVolumes().get(0));
            listaVolume.get(0).getDisponivel();
            System.out.println(idDispositivo);
            System.out.println("Teste Disco");
            String sql3 = (String.format("INSERT INTO disco (modelo, numero_serial, tamanho, fk_dispositivo) VALUES ('%s', '%s', %.0f, %d)",
                    modelo, serial, Double.valueOf(String.valueOf(tamanho).substring(0, 3).toString()), idDispositivo));
            conexaoNuvem.getJdbcTemplate().update(sql3);
            conexaoLocal.getConnectionTemplate().update(sql3);
        }

    }

    public void processos() {
        //yohan yucatan
    }

    public Double memoria() {

        System.out.println("Teste Memoria");
        Double memoriaUso = Double.longBitsToDouble(looca.getMemoria().getEmUso());
        Double memoriaTotal = Double.longBitsToDouble(looca.getMemoria().getTotal());
        Double memoriaUsoPorc = (memoriaUso / memoriaTotal) * 100;

        if (memoriaUsoPorc > 40) {
            Slack.mensagemSlack(String.format("Novo teste uso de memoria chegou em %.2f", memoriaUsoPorc));
        }

        return memoriaUsoPorc;
    }

    public Double disco() {

        Locale.setDefault(Locale.US);

        Double tamanhoTotal;
        Double tamanhoDisponivel;
        Double emUso;
        Double discoUsoPorc;

        tamanhoTotal = (double) disco.getDiscos().get(0).getTamanho();
        tamanhoDisponivel = (double) disco.getVolumes().get(0).getDisponivel();
        emUso = tamanhoTotal - tamanhoDisponivel;
        discoUsoPorc = (emUso / tamanhoTotal) * 100;

        return discoUsoPorc;
    }

    public Double processador() {
        return looca.getProcessador().getUso();
    }

    public void temperatura(Integer idDispositivo) {
       
            System.out.println("Teste especificação");
            String sql = (String.format("INSERT INTO historico (fk_dispositivo, fk_tipo_metrica, registro) VALUES ('%d', '4', %.1f)", idDispositivo, looca.getTemperatura().getTemperatura()));
            Locale.setDefault(Locale.US);
           conexaoNuvem.getJdbcTemplate().update(sql);
            conexaoLocal.getConnectionTemplate().update(sql);
        
    }

    public Boolean login(String email, String senha) {
       
        Map<String, Object> usuarioMomento =  usuario.recuperar(this.usuario, email, senha);
        
        

        if (Integer.valueOf(String.valueOf(usuarioMomento.get("id"))) > 0) {
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
