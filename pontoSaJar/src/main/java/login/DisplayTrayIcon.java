/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import com.pontosa.jar.database.ConexaoLocal;
import com.pontosa.jar.database.ConexaoNuvem;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author yu_mi
 */
public class DisplayTrayIcon {
    
    private static ConexaoLocal conexaoLocal = new ConexaoLocal();
    private static ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
    
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
                
                
              
            System.out.println("Teste bater ponto");
            
           String sql = (String.format("INSERT INTO ponto(id, saida, fk_usuario) VALUES(null, null, 1)"));
               conexaoNuvem.getJdbcTemplate().update(sql);
            conexaoLocal.getConnectionTemplate().update(sql);
            }
        });
        
          baterPontoS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  
            
            System.out.println("Teste sair ponto");
                Date data = new Date();
                    String dataUpdate = (data.toInstant().toString().substring(0, data.toInstant().toString().indexOf("T")) + '%').toString();
                    System.out.println(dataUpdate);
                    //dataUpdate += '%';
            
                
           String sql = (String.format("update ponto set saida = default where fk_usuario = 1 and entrada like '%s'", dataUpdate));
           
               conexaoNuvem.getJdbcTemplate().update(sql);
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
