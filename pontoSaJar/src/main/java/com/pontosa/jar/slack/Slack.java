/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pontosa.jar.slack;

import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

/**
 * @author User
 */
public class Slack {

    private static String webHooksUrl = "https://hooks.slack.com/services/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";

    private static String oAuthToken = "xoxb-4318388465536-4294737522802-b90N8J7zMgQVxrey7PeohOJs";
    private static String slackChannel = "#3-sprint";

    public static void mensagemSlack(String mensagem) {
        try {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(mensagem);

            Payload payLoad = Payload.builder().channel(slackChannel).text(msgBuilder.toString()).build();

            WebhookResponse wbResp = com.slack.api.Slack.getInstance().send(webHooksUrl, payLoad);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}