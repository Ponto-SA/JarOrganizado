package com.pontosa.jar.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogError {

    public LogError() {

    }

    Locale brasil = new Locale("pt", "BR");
    DateFormat hora = DateFormat.getTimeInstance();
    DateFormat dataLog = DateFormat.getDateInstance(DateFormat.FULL, brasil);
    Calendar dia = Calendar.getInstance();
    Date data = dia.getTime();

    public void logError(String texto) throws IOException {
        FileWriter arq = new FileWriter("./logs/Log[" + dataLog.format(data) +"].txt");
        PrintWriter gravarArq = new PrintWriter(arq);

        gravarArq.printf(texto);

        arq.close();
    }
}
