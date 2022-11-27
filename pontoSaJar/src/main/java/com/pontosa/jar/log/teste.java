package com.pontosa.jar.log;

/**
 *
 * @author Administrador
 */
public class teste {
    public static void main(String[] args) {
        LogError log = new LogError("Executor");
        
        log.adicionarLog("Erro ao buscar dados no banco Azure");
    }
}
