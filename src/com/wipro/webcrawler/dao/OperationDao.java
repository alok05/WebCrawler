/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.webcrawler.dao;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author YADAV
 */
public class OperationDao extends BaseDao {

    public Connection conn = null;
    static Logger log = Logger.getLogger(OperationDao.class);

    public OperationDao() {
    }
    /*
     * This method is used to check if url is already in database or not
     */

    public boolean checkIfDomainExists(String URL) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement psmt = null;
        boolean hasRecord = false;
        String sql = "select URL from Record where URL = '" + URL + "'";
        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            if (rs.next()) {
                hasRecord = true;
                log.info(URL + " Already Exists");
            }

        } catch (SQLException ex) {
            log.debug("SQLException in method checkIfDomainExists", ex);
            throw ex;
        } catch (Exception ex) {
            log.debug("Exception in method checkIfDomainExists", ex);
            throw ex;
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                log.error("Exception in closing in method ConnectioncheckIfDomainExists", ex);
            }
        }
        return hasRecord;
    }
    /*
     * This method is truncate data from table Record
     */

    public boolean truncateRecord() throws SQLException, Exception {
        boolean isTruncated = false;
        PreparedStatement psmt = null;
        String sql = "TRUNCATE table Record";
        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.execute();
            isTruncated = true;
            log.info("Truncated Record");
        } catch (SQLException ex) {
            log.debug("SQLException in method truncateRecord", ex);
            throw ex;
        } catch (Exception ex) {
            log.debug("Exception in method truncateRecord", ex);
            throw ex;
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                log.error("Exception in closing in method truncateRecord", ex);
            }
        }
        return isTruncated;
    }
    /*
     * This method is used to insert record in table Record
     */

    public void insertRecord(String URL) throws SQLException, Exception {
        PreparedStatement psmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO  `crawler`.`Record` " + "(`URL`) VALUES " + "(?)";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, URL);
            psmt.execute();
            log.info("Inserted URL " + URL);
        } catch (SQLException ex) {
            log.debug("SQLException in method insertRecord", ex);
            throw ex;
        } catch (Exception ex) {
            log.debug("Exception in method insertRecord", ex);
            throw ex;
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                log.error("Exception in closing connection in method insertRecord", ex);
            }
        }
    }
    /*
     * This method is used to get all record from table Record and writin in to file
     */

    public void writeToFile() throws SQLException, Exception {
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "Select URL from Record";
            psmt = conn.prepareStatement(sql);
            BufferedWriter bfwr = new BufferedWriter(new FileWriter("crawler.txt"));
            rs = psmt.executeQuery();
            while (rs.next()) {
                bfwr.write(rs.getString("URL"));
                bfwr.newLine();
            }
            bfwr.flush();
            bfwr.close();
            //first check if Desktop is supported by Platform or not
            if (Desktop.isDesktopSupported()) {
                File file = new File("crawler.txt");
                Desktop desktop = Desktop.getDesktop();
                if (file.exists()) {
                    desktop.open(file);
                }
            }
            log.info("URLs written successfully in method writeToFile");
        } catch (SQLException ex) {
            log.debug("SQLException in method writeToFile", ex);
            throw ex;
        } catch (Exception ex) {
            log.debug("Exception in method writeToFile", ex);
            throw ex;
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                log.error("Exception in closing Connection in method writeToFile", ex);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
}