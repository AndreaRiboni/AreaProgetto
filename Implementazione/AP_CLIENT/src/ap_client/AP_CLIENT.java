/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riboni.andrea
 */
public class AP_CLIENT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Invio");
            String risp = null;
            //REGISTRAZIONE
            //risp = ClientConnector.request("002;admin@admin.it;admin;admin;admin;3453797411;45.46, 9.19;0");
            
            //AGGIUNTA LOCALE
            risp = ClientConnector.request("016;Da Totto;45.46;9.19;3663574756;1");
            
            
            if(!risp.equalsIgnoreCase("null")) System.out.println(risp);
            System.out.println("Inviata");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClientException ex) {
            ex.printStackTrace();
        }
    }
    
}
