package com.pontosa.jar.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LogError {

    private String nomeLog;

    public LogError(String nomeLog) {
        this.nomeLog = nomeLog;
    }

    public void adicionarLog(String error) {
        Locale brasil = new Locale("pt", "BR");
        DateFormat hora = DateFormat.getTimeInstance();
        DateFormat dataLog = DateFormat.getDateInstance(DateFormat.FULL, brasil);
        Calendar dia = Calendar.getInstance();
        Date data = dia.getTime();
        String dataNow = dataLog.format(data);
        String horaNow = hora.format(data);
        try {
            this.createLogArq("\n[" + dataNow + " - " + horaNow + "] - erro - " 
                    + error);
        } catch (Exception e) {
            
        }
    };
    
    private void createLogArq(String texto) throws IOException {

        Calendar cal = Calendar.getInstance();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer anoAtual = cal.get(Calendar.YEAR);
        Integer mesAtual = cal.get(Calendar.MONTH) + 1; //O primeiro mês começa com 0
        Integer diaAtual = cal.get(Calendar.DAY_OF_MONTH);
        Integer horaAtual = cal.get(Calendar.HOUR_OF_DAY);
        Integer minutoAtual = cal.get(Calendar.MINUTE);
        Integer segundoAtual = cal.get(Calendar.SECOND);
        String momentoAtual = String.format("-%d-%d-%d", horaAtual,
                minutoAtual, segundoAtual);
        
        File D = new File("./LogsPontoSa"); 
        String pathLogCrate = "./LogsPontoSa/Classe-" + nomeLog + "["
                + anoAtual + mesAtual + diaAtual + "]";
        File pathLog = new File(pathLogCrate);
        D.mkdir();
        pathLog.mkdir();
        FileWriter arq = new FileWriter( pathLogCrate +"/log-" + nomeLog + "["
                + anoAtual + mesAtual + diaAtual + momentoAtual + "].txt", true);

        PrintWriter gravarArq = new PrintWriter(arq);

        gravarArq.printf(String.format("%s", texto));

        arq.close();
    }
}