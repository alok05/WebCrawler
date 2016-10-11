/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.webcrawler.dao;

import org.apache.log4j.Logger;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Before;
import org.junit.After;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.junit.runner.RunWith;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ALOK YADAV
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseDao.class})
public class OperationDaoTest {
    Connection con;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    SQLException ex;
    BaseDao baseDao;
    Logger logger;
    
    OperationDao operationDao = new OperationDao();
    public OperationDaoTest() {
        
    }
      
    @Before
    public void setUp() throws Exception {
        
        preparedStatement = PowerMockito.mock(PreparedStatement.class);
        con = PowerMockito.mock(Connection.class);
        resultSet = PowerMockito.mock(ResultSet.class);
        baseDao = PowerMockito.mock(BaseDao.class);
        PowerMockito.mockStatic(BaseDao.class);
        logger = PowerMockito.mock(Logger.class);
        PowerMockito.when(baseDao.getConnection()).thenReturn(con);
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
    }
    
    @After
    public void tearDown() throws Exception {
        con = null;
        preparedStatement = null;
        resultSet = null;
        operationDao = null;
        ex = null;
        baseDao = null;
    }

    /**
     * Test of checkIfDomainExists method, of class OperationDao.
     */
    @Test(expected = NullPointerException.class)
    public void testCheckIfDomainExistsIfConnectionIsNull() throws Exception {
        String URL = "www.wipro.com";        
        boolean expResult = false;
        PowerMockito.when(baseDao.getConnection()).thenReturn(con); 
        boolean result = operationDao.checkIfDomainExists(URL);
        assertEquals(expResult, result);
        
    }
    /**
     * Test of checkIfDomainExists method, of class OperationDao.
     */
    @Test
    public void testCheckIfDomainExistsForNewURL() throws Exception {
        String URL = "www.wipro.com";        
        boolean expResult = false;      
        PowerMockito.when(con.prepareStatement("select URL from Record where URL = 'www.wipro.com'")).thenReturn(preparedStatement);
        boolean result = operationDao.checkIfDomainExists(URL);
        assertEquals(expResult, result);
        
    }
    /**
     * Test of truncateRecord method, of class OperationDao.
     */
    @Test(expected = SQLException.class)
    public void testTruncateRecordIfSQLExceptionOccurred() throws Exception {
        String sql = "TRUNCATE table Record";
        PowerMockito.when(con.prepareStatement(sql)).thenThrow(SQLException.class);
        boolean result = operationDao.truncateRecord();
    }
    /**
     * Test of truncateRecord method, of class OperationDao.
     */
    @Test
    public void testTruncateRecord() throws Exception {
        boolean expResult = true;
        String sql = "TRUNCATE table Record";
        PowerMockito.when(con.prepareStatement(sql)).thenReturn(preparedStatement);
        PowerMockito.when(preparedStatement.execute()).thenReturn(true);
        boolean result = operationDao.truncateRecord();
        assertEquals(expResult, result);        
    }
    /**
     * Test of insertRecord method, of class OperationDao.
     */
    @Test(expected = SQLException.class)
    public void testInsertRecordForException() throws Exception {
        String URL = "";
        PowerMockito.when(con.prepareStatement("INSERT INTO  `crawler`.`Record` " + "(`URL`) VALUES " + "(?)")).thenThrow(SQLException.class);
        operationDao.insertRecord(URL);        
        PowerMockito.verifyNoMoreInteractions(con);
    }
    /**
     * Test of insertRecord method, of class OperationDao.
     */
    @Test
    public void testInsertRecord() throws Exception {
        String URL = "www.wipro.com";
        PowerMockito.when(con.prepareStatement("INSERT INTO  `crawler`.`Record` " + "(`URL`) VALUES " + "(?)")).thenReturn(preparedStatement);
        PowerMockito.when(preparedStatement.execute()).thenReturn(true);
        operationDao.insertRecord(URL);       
        
    }
    /**
     * Test of writeToFile method, of class OperationDao.
     */
    @Test(expected = NullPointerException.class)
    public void testWriteToFileForException() throws Exception {
        String sql = "Select URL from Record";        
        PowerMockito.when(con.prepareStatement(sql)).thenReturn(preparedStatement); 
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(null);
        operationDao.writeToFile();                
    }    
    
}
