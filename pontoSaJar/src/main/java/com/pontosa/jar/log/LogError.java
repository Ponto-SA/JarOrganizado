package com.pontosa.jar.log;

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
            this.createLogArq("\n[" + dataNow + " - " + horaNow + "] - " + error);
        } catch (Exception e) {
        }
    };
    
    private void createLogArq(String texto) throws IOException {

        Calendar cal = Calendar.getInstance();
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer anoAtual = cal.get(Calendar.YEAR);
        Integer mesAtual = cal.get(Calendar.MONTH) + 1; //O primeiro mês começa com 0
        Integer diaAtual = cal.get(Calendar.DAY_OF_MONTH);

        FileWriter arq = new FileWriter("./logs/log-" + nomeLog + "["
                + anoAtual + mesAtual + diaAtual + "].txt", true);

        PrintWriter gravarArq = new PrintWriter(arq);

        gravarArq.printf(String.format("%s", texto));

        arq.close();
    }
}