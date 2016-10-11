/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.webcrawler.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author ALOK YADAV
 */
public class BaseDao {
    /*
     * variable to hold the database url parameter as obtained from properties
     * file
     */

    private static String db_url = "";
    /*
     * variable to hold the password of database as obtained from properties
     * file
     */
    private static String password = "";
    /* variable to hold the user of database as obtained from properties file */
    private static String user = "";
    /* Creating ResourceBundle reference */
    private static ResourceBundle resourceBundle;
    private static Connection connection = null;
    private static String DRIVER = "";
    static Logger log = Logger.getLogger(BaseDao.class);
    
    public BaseDao (){
        String propFileName = "properties.dbDetails";
        resourceBundle = ResourceBundle.getBundle(propFileName);
    }
   

    /**
     * This method is responsible to create connection with the database. It
     * reads the database properties file to read the JDBC connection parameters
     * and establishes a connection to it.
     * 
     * @return conn returns Connection @see {java.sql.Connection}
     * 
     */
    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed() || !connection.isValid(0)) {
                
                db_url = "" + resourceBundle.getString("db.url").trim();
                user = resourceBundle.getString("db.user").trim();
                password = resourceBundle.getString("db.password").trim();
                DRIVER = "" + resourceBundle.getString("db.class");
                Class.forName(DRIVER).newInstance();
                connection = DriverManager.getConnection(db_url, user, password);

            }

        } catch (SQLException se) {
            log.error("Unable to connect with Database.", se);
            JOptionPane.showMessageDialog(null,
                    "Unable to connect with Database.", "Connection Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            log.error("Unable to connect with Database.", ex);
            JOptionPane.showMessageDialog(null,
                    "Unable to connect with Database.", "Connection Error",
                    JOptionPane.ERROR_MESSAGE);

        }
        return connection;
    }
}
