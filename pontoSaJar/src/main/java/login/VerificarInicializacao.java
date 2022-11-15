/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author User
 */
public class VerificarInicializacao {
    public static void main(String[] args) {
         try {
                File teste = new File("./verify.txt");
                if (teste.createNewFile()){
                    System.out.println("File criado: " + teste.getName());
                     try {
                         FileWriter myWriter = new FileWriter("./verify.txt");
                         myWriter.write("138.249.195");  
                         myWriter.close();
                         System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
                }else {
            
                     System.out.println("Arquivo ja existe");
     }
  
               

                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
 public Integer teste (){
     System.out.println("Reading File from Java code");
       //Name of the file
       String fileName="verify.txt";
                     try{

          //Create object of FileReader
           FileReader inputFile = new FileReader(fileName);

          //Instantiate the BufferedReader Class
           BufferedReader bufferReader = new BufferedReader(inputFile);

          //Variable to hold the one line data
          String line;
          String testeRead = "";
          // Read file line by line and print on the console
          while ((line = bufferReader.readLine()) != null)   {
            testeRead = line;
          }
          //Close the buffer reader
          bufferReader.close();
          String numeros = "";
           for (int i = 0; i < testeRead.length(); i++){
                Character letra = testeRead.charAt(i);
               if (!letra.toString().equals(".")){
                numeros += letra;
               } 
              
           }
           if (Integer.valueOf(numeros) / 3 == 46083065){
               System.out.println("Sucesso");
               return 1;
               
           } else {
               System.out.println("Falhou");
           }
                  
           
       }catch(Exception e){
          System.out.println("Error while reading file line by line:" + e.getMessage());                      
       }
    return 0;
 }

    private void dispatchEvent(WindowEvent windowEvent) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}