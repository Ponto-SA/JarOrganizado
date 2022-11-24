package com.pontosa.jar;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.pontosa.jar.database.ConexaoNuvem;
import com.pontosa.jar.usuario.Dispositivo;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessosGroup;
import com.github.britooo.looca.api.group.discos.DiscosGroup;
import com.github.britooo.looca.api.group.discos.Volume;
import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.usuario.TesteEmLote;
import com.pontosa.jar.usuario.Usuario;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.api.ApiTestResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Executor { 
    public static void main(String[] args) throws InterruptedException, IOException {
        Date data = new Date();
        String dataUpdate = (data.toInstant().toString().substring(0, data.toInstant().toString().indexOf("T")) + "%").toString();
        System.out.println(dataUpdate);
    }        
}
