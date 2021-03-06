package ap_database;

import ap_server.AP_SERVER;
import ap_utility.ConfigurationLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Andrea Riboni
 */
public class Database {

    private Connection conn;
    private Statement query;
    private PreparedStatement PrepQuery;
    private ResultSet result;
    private org.apache.log4j.Logger log;
    private static String NOME_DB, USER, IP, PSW;

    public Database() {
        log = AP_SERVER.log;
        NOME_DB = ConfigurationLoader.getNodeValue("dbname");
        USER = ConfigurationLoader.getNodeValue("dbuser");
        IP = ConfigurationLoader.getNodeValue("dbip");
        PSW = ConfigurationLoader.getNodeValue("dbpsw");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + IP + ":3306/" + NOME_DB + "?user=" + USER + "&password=" + PSW);
            query = conn.createStatement();
        } catch (SQLException ex) {
            log.error("Connessione al database fallita.");
        }
    }

    public ArrayList<String[]> stringify(ResultSet res) {
        try {
            if(res==null){
                log.warn("si è cercato di effettuare operazioni su un resultset vuoto");
                return null;
            }
            int columns = res.getMetaData().getColumnCount();
            ArrayList<String[]> table = new ArrayList<>();
            while (res.next()) {
                String[] row = new String[columns];
                for (int i = 0; i < columns; i++) {
                    row[i] = res.getString(i + 1);
                }
                table.add(row);
            }
            return table;
        } catch (SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public ResultSet select(String query) {
        try {
            return this.query.executeQuery(query);
        } catch (SQLException ex) {
            log.error(ex);
            return null;
        }
    }

    public int count(ResultSet res) {
        try {
            res.last();
            return res.getRow();
        } catch (Exception ex) {
            log.error(ex);
            return 0;
        }
    }

    public boolean register(String[] msg) {
        try {
            query.executeUpdate(String.format("insert into Utente (Nome, Cognome, Cellulare, Indirizzo, Mail, Psw, Privilegio)"
                    + "values ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", msg[3], msg[4], msg[5], msg[6], msg[1], msg[2], msg[7]));
        } catch (SQLException ex) {
            log.error("REGISTRAZIONE: "+ex);
            return false;
        }
        log.info("un utente si è registrato");
        return true;
    }

    public boolean addLocale(String[] msg) {
        try {
            query.executeUpdate(String.format("insert into Locale (Nome, Indirizzo, Cellulare, IDAdmin, Punteggio, NumRecensioni)"
                    + "values ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s', '%6$s')", msg[1], msg[2] + ", " + msg[3], msg[4], msg[5], "0", "0"));
        } catch (SQLException ex) {
            log.error(ex);
            return false;
        }
        return true;
    }

    public int update(String query) {
        try {
            System.out.println(query);
            return this.query.executeUpdate(query);
        } catch (SQLException ex) {
            log.error(ex);
            return -1;
        }
    }

    public boolean userIdExists(String id) {
        return count(select("select * from Utente where ID = " + id)) > 0;
    }

    public String[] getFirstRow(ResultSet res) {
        try {
            res.next();
            int col = res.getMetaData().getColumnCount();
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < col; i++) {
                row.append(res.getString(i + 1)).append("%%%");
            }
            return row.toString().split("%%%");
        } catch (Exception ex) {
            log.error(ex);
        }
        return null;
    }

    public String getIdFattorinoFromLocale(String id) {
        System.out.println(id);
        return getFirstRow(select("select IDCliente from Fattorini where IDLocale = " + id))[0];
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            log.error("impossibile chiudere la connessione al db");
        }
    }

}
