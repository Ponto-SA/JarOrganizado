/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pontosa.jar.login;

import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.database.ConexaoNuvem;
import com.pontosa.jar.usuario.Usuario;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author yu_mi
 */
public class DisplayTrayIcon {
    
    private static ConexaoLocal conexaoLocal = new ConexaoLocal();
    private static ConexaoNuvem conexaoNuvem = new ConexaoNuvem();

    private static Usuario usuario = new Usuario();
    
    static TrayIcon trayIcon;
    
    public DisplayTrayIcon() {
        ShowTrayIcon();
    }
    
    public static void ShowTrayIcon() {
    
        if(!SystemTray.isSupported()){
            System.out.println("Error");
            System.exit(0);
            return;
        }

        final PopupMenu popup = new PopupMenu();        
          try {
            InputStream inputStream= ClassLoader.getSystemClassLoader().getResourceAsStream("assets/quad1.png");
//or getResourceAsStream("/images/Graph.png"); also returns inputstream

            BufferedImage img = ImageIO.read(inputStream);
    trayIcon = new TrayIcon(img, "S.A. company", popup);
}
   catch (IOException e) {}
        final SystemTray tray = SystemTray.getSystemTray();
        
        
        trayIcon.setToolTip("Company S.A.");
        
        MenuItem baterPontoE = new MenuItem("Marcar Entrada");
        popup.add(baterPontoE);
        popup.addSeparator();
        MenuItem baterPontoS = new MenuItem("Marcar Saida");
        popup.add(baterPontoS);
        popup.addSeparator();
          MenuItem exit = new MenuItem("Exit");
        popup.add(exit);
        
        trayIcon.setPopupMenu(popup);
        
        baterPontoE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Map<String, Object> usuarioMomento = usuario.recuperarIdUsuario();
                Integer id = Integer.valueOf(String.valueOf(usuarioMomento.get("id")));

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
              
            System.out.println("Teste bater ponto");
            
           String sql = (String.format("INSERT INTO ponto(entrada, saida, fk_usuario) VALUES('%s', null, %d)", dtf.format(LocalDateTime.now()), id));
               conexaoNuvem.getJdbcTemplate().update(sql);
               conexaoLocal.getConnectionTemplate().update(sql);
            }
        });
        
          baterPontoS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Map<String, Object> usuarioMomento = usuario.recuperarIdUsuario();
                Integer id = Integer.valueOf(String.valueOf(usuarioMomento.get("id")));
            
            System.out.println("Teste sair ponto");
                Date data = new Date();
                    String dataUpdate = (data.toInstant().toString().substring(0, data.toInstant().toString().indexOf("T")) + "%").toString();
                    System.out.println(dataUpdate);
                    //dataUpdate += '%';

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            
                String sql = (String.format("update ponto set saida = '%s' where fk_usuario = %d and entrada like '%s'", dtf.format(LocalDateTime.now()), id, dataUpdate));

                String sql2 = (String.format("UPDATE ponto SET saida = '%s' WHERE fk_usuario = %d AND Convert(VARCHAR(50),entrada, 126) like '%s'", dtf.format(LocalDateTime.now()), id, dataUpdate));

               conexaoNuvem.getJdbcTemplate().update(sql2);
               conexaoLocal.getConnectionTemplate().update(sql);
            }
        });
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        
        
        try {
        tray.add(trayIcon);
        }catch (AWTException e){
            
        }
        
    }
    
    protected static Image CreateIcon(String path, String desc){
    
        URL ImageURL = DisplayTrayIcon.class.getResource(path);
        return (new ImageIcon(ImageURL, desc)).getImage();
    }
    
}
