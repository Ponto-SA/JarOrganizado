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
import com.pontosa.jar.log.LogError;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;

public class Dispositivo {
    LogError log = new LogError("Dispositivo");
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
                    "select * from dispositivo where host_name = ?", getHostName());

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format(
                    "Não foi encontrado nenhum HostName: %s",
                    e.getStackTrace()));
            return null;
        }
    }

    public Map<String, Object> verificarDisco(Integer idDispositivo) {
        try {
            Map<String, Object> registro = conexaoNuvem.getJdbcTemplate().queryForMap(
                    "select * from disco where fk_dispositivo = ?", idDispositivo);

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format(
                    "Não foi encontrado nenhum disco: %s",
                    e.getStackTrace()));
            return null;
        }
    }

    public Map<String, Object> verificarDispositivoLocal(Integer idDispositivo) {
        try {
            Map<String, Object> registro = conexaoLocal.getConnectionTemplate().queryForMap(
                    "select * from dispositivo where id = ?", idDispositivo);

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format(
                    "Não foi encontrado nenhum Dispositvo Local: %s",
                    e.getStackTrace()));
            return null;
        }
    }

    public void loopRegistro() throws InterruptedException {
        
        try {
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
        } catch (NullPointerException e) {
            log.adicionarLog(String.format(
                    "Dispositivo associado não encontrado: %s",
                    e.getStackTrace()));
        }
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
        String marca = String.valueOf(dispositivoMomento.get("marca"));
        String modeloDisp = String.valueOf(dispositivoMomento.get("modelo"));
        String hostname = String.valueOf(dispositivoMomento.get("host_name"));

        Map<String, Object> dispositivoLocal = verificarDispositivoLocal(idDispositivo);
        System.out.println("Printando dispositivo Local");
        System.out.println(dispositivoLocal);
        Map<String, Object> discoExiste = verificarDisco(idDispositivo);

        if (dispositivoLocal == null) {
            String sql = String.format("INSERT INTO dispositivo(id, marca, modelo, host_name) VALUES (%d, '%s', '%s', '%s');", idDispositivo,
                    marca, modeloDisp, hostname);
            conexaoLocal.getConnectionTemplate().update(sql);
        }

        System.out.println("Teste especificação");
        Double memoriaTotal = Double.valueOf((looca.getMemoria().getTotal() / 1024) / 1024) / 1024;
        Locale.setDefault(Locale.US);
        System.out.println(memoriaTotal);
        String sql = (String.format("UPDATE dispositivo SET tipo_processador = '%s', memoria_total = '%.2f', sistema_operacional = '%s' WHERE host_name = '%s'",
                looca.getProcessador().getNome(), memoriaTotal, looca.getSistema().getSistemaOperacional(), getHostName()));
        conexaoNuvem.getJdbcTemplate().update(sql);
        conexaoLocal.getConnectionTemplate().update(sql);

        Locale.setDefault(Locale.US);
        System.out.println(disco.getDiscos());
        System.out.println(disco.getDiscos().size());
//            String modelo = disco.getDiscos().get(0).getModelo().substring(0, disco.getDiscos().get(0).getModelo().indexOf("("));
//            String serial = disco.getDiscos().get(0).getSerial();
        Long tamanho = (disco.getDiscos().get(0).getTamanho());
        System.out.println(tamanho);
        listaVolume.add(disco.getVolumes().get(0));
        listaVolume.get(0).getDisponivel();
        System.out.println(idDispositivo);
        System.out.println("Teste Disco");

        if (discoExiste == null) {
            String sql3 = (String.format("INSERT INTO disco (tamanho, fk_dispositivo) VALUES (%.0f, %d)",
                    Double.valueOf(String.valueOf(tamanho).substring(0, 3).toString()), idDispositivo));
            conexaoNuvem.getJdbcTemplate().update(sql3);
            conexaoLocal.getConnectionTemplate().update(sql3);
        }
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

        Map<String, Object> usuarioMomento = usuario.recuperar(email, senha);
        Integer id = Integer.valueOf(String.valueOf(usuarioMomento.get("id")));
        String nome = String.valueOf(usuarioMomento.get("nome"));
        String sobrenome = String.valueOf(usuarioMomento.get("sobrenome"));
        String emailAchado = String.valueOf(usuarioMomento.get("email"));
        String senhaAchada = String.valueOf(usuarioMomento.get("senha"));
        String fk_chefe = null;
        Integer status = Integer.valueOf(String.valueOf(usuarioMomento.get("status")));

        System.out.println("Antes de verificar Localmente");
        Map<String, Object> usuarioLocal = usuario.verificarLocalmente(id);
        System.out.println("Depois de verificar Localmente");

        if (id > 0) {
            System.out.println("UsuarioExiste");
            if (usuarioLocal == null) {
                System.out.println("Entrou no if null");
                String sql = (String.format("INSERT INTO usuario VALUES (%d, '%s', '%s', '%s', '%s', %d, %s);", id, nome, sobrenome, emailAchado,
                        senhaAchada, status, fk_chefe));
                conexaoLocal.getConnectionTemplate().update(sql);
                System.out.println("Executou insert");
            }
            return true;
        } else {
            return false;
        }

    }

    public String getHostName() {
        try {
            return hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.adicionarLog(String.format("HostName não encontrado: %s", e.getStackTrace()));
            return null;
        }
    }

    public String getHostAddress() {
        try {
            return hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.adicionarLog(String.format(
                    "HostAddress não encontrado: %s", e.getStackTrace()));
            return null;
        }
    }
}
