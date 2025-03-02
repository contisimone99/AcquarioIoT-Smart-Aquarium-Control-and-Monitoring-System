import java.io.FileWriter;
import java.lang.Thread.State;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;;

public class JDBCDriver {

    Connection c = null;
    Statement stmt = null;
    String DB_name;

    public JDBCDriver(String DB_name) {
        this.DB_name = DB_name;
    }

    private void startConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("Connessione al DB avvenuta con successo");
    }

    private void closeConnection(){
        try {
            c.commit();
            c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
    }

    public JSONObject getDomainInfo(){
    	JSONObject objDomain = new JSONObject();
    	
        startConnection();
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sqlUsers = "SELECT * from users";
            String sqlServices = "SELECT * from services";
            String sqlTopics = "SELECT * from topics";
            
            KeycloakHandler keycloak = new KeycloakHandler();
        	JSONArray users = keycloak.getUsersInfo();
           /* ResultSet resultSetUsers = stmt.executeQuery(sqlUsers);
            while (resultSetUsers.next()) {
            	JSONObject objUser = new JSONObject();
                objUser.put("user",resultSetUsers.getString("users"));
                objUser.put("role",resultSetUsers.getString("role"));
                objUser.put("passwd",resultSetUsers.getString("passwd"));
                users.put(objUser);
                //System.out.println(objUser.toString(1));
            }*/
            JSONArray services = new JSONArray();
            ResultSet resultSetServices = stmt.executeQuery(sqlServices);
            while (resultSetServices.next()) {
            	JSONObject objService = new JSONObject();
                objService.put("service",resultSetServices.getString("service"));
                objService.put("host",resultSetServices.getString("host"));
                objService.put("uri",resultSetServices.getString("uri"));
                services.put(objService);
            }
            JSONArray topics = new JSONArray();
            ResultSet resultSetTopics = stmt.executeQuery(sqlTopics);           
            while (resultSetTopics.next()) {
            	JSONObject objTopic = new JSONObject();
            	objTopic.put("role", resultSetTopics.getString("role"));
                objTopic.put("topic", resultSetTopics.getString("topic"));
                topics.put(objTopic);
            }
       
            
            stmt.close();
            closeConnection();
            
            objDomain.put("domain", "gruppo3");
            objDomain.put("services", services);
            objDomain.put("users", users);
            objDomain.put("topics", topics);
            
            
            FileWriter file;
            try {
				file = new FileWriter("./conf.json");
				file.write(objDomain.toString(1));
				file.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Errore scrittura del file");

			}
            System.out.println(objDomain.toString(1));
            return objDomain;
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeConnection();
        }
        return null;
    }
    // Ricordarsi di mettere variabile Connection e Statement come variabile di
    // classe, e nell'eventuale costruttore della classe
    // mettiamo il codice che tiene aperta la connessione al db e aggiungiamo
    // eventuale metodo per chiusura connessione

    // metodo tipo per eventuale insert
    public static void InsertTopics(String role, String topic) {
        Statement stmt = null;
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
            System.out.println("Connessione al DB avvenuta con successo");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO TOPICS(role,topic)" +
                    "VALUES('" + role + "', '" + topic + "');";

            stmt.executeUpdate(sql);
            System.out.println("primo insert");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(-1);
        }
        System.out.println("record creati con successo");
    }

    // metodo tipo per eventuale delete
    public static void DeleteTopics(String role, String topic) {
        Statement stmt = null;
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
            System.out.println("Connessione al DB avvenuta con successo");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "DELETE from TOPICS where role='" + role + "' and topic='" + topic + "'";

            stmt.executeUpdate(sql);
            System.out.println("primo insert");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(-1);
        }
        System.out.println("record creati con successo");
    }

    // metodo tipo per eventuale update
    public static void UpdateTopics(String role, String topic) {
        Statement stmt = null;
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
            System.out.println("Connessione al DB avvenuta con successo");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE TOPICS set role='" + role + "' where topic='" + topic + "'";

            stmt.executeUpdate(sql);
            System.out.println("primo insert");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(-1);
        }
        System.out.println("record creati con successo");
    }

    public static void SelectTopics(String role) {
        Statement stmt = null;
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
            System.out.println("Connessione al DB avvenuta con successo");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT * from TOPICS where role='" + role + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String topic = resultSet.getString("topic");
                String roles = resultSet.getString("role");
                System.out.println("Topic: " + topic + " for role: " + roles);
            }
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void InsertUsers(JSONArray users) {
        Statement stmt = null;
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("test");
            c = DriverManager.getConnection("jdbc:sqlite:DB_acquario.db");
            System.out.println("Connessione al DB avvenuta con successo");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            for(int i=0;i<users.length();i++) {
            	JSONObject user = users.getJSONObject(i);
            String sql = "INSERT INTO users(users,role,passwd)" +
                    "VALUES('" + user.getString("username") + "', '" + user.getString("role") + "', '" + user.getString("password") + "');";
            stmt.executeUpdate(sql);
            System.out.println("insert");
            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(-1);
        }
        System.out.println("record creati con successo");
    }

}
