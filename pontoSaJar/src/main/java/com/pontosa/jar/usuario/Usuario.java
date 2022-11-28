/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pontosa.jar.usuario;

import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.database.ConexaoNuvem;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author User
 */
public class Usuario {
    LogError log = new LogError("Usuario");
    private Integer id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private Integer status;
    private Integer fkChefe;

    private static ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
    private ConexaoLocal conexaoLocal = new ConexaoLocal();
    private static Dispositivo dispositivo = new Dispositivo();

    public Usuario() {
    }

    ;
    
    public Usuario(Integer id, String nome, String sobrenome, String email, String senha, Integer status, Integer fkChefe) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.status = status;
        this.fkChefe = fkChefe;
    }

    public Map<String, Object> recuperar(String email, String senha) {
        try {
            Map<String, Object> registro = conexaoNuvem.getJdbcTemplate().queryForMap(
                    "select * from usuario where email = ? and senha = ?", email, senha);

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format("Erro ao logar: %s", e.getStackTrace()));
            return null;
        }
    }

    public static Map<String, Object> recuperarIdUsuario() {
        LogError log = new LogError("Usuario");
        try {
            Map<String, Object> registro = conexaoNuvem.getJdbcTemplate().queryForMap(
                    "select usuario.id from usuario join usuario_maquina on usuario_maquina.fk_usuario = usuario.id join dispositivo on usuario_maquina.fk_dispositivo = dispositivo.id where dispositivo.host_name = ? and usuario_maquina.ativo = 1;", dispositivo.getHostName());

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format("Erro ao logar: %s", e.getStackTrace()));
            return null;
        }
    }

    public Map<String, Object> verificarLocalmente(Integer idUsuario) {
        try {
            Map<String, Object> registro = conexaoLocal.getConnectionTemplate().queryForMap(
                    "select * from usuario where id = ? ", idUsuario);

            return registro;
        } catch (EmptyResultDataAccessException e) {
            log.adicionarLog(String.format("Erro ao logar: %s", e.getStackTrace()));
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFkChefe() {
        return fkChefe;
    }

    public void setFkChefe(Integer fkChefe) {
        this.fkChefe = fkChefe;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", email=" + email + ", senha=" + senha + ", status=" + status + ", fk_chefe=" + fkChefe + '}';
    }
}
