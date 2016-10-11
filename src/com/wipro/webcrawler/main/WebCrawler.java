/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.webcrawler.main;

import com.wipro.webcrawler.dao.OperationDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.log4j.BasicConfigurator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ALOK YADAV
 */
public class WebCrawler {

    public static OperationDao operationDao = new OperationDao();
    /* Creating ResourceBundle reference */
    private static ResourceBundle resourceBundle;
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static String domain;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String propFileName = "properties.domain";
        resourceBundle = ResourceBundle.getBundle(propFileName);
        BasicConfigurator.configure();
        try {
        //truncate record in table Record
        operationDao.truncateRecord();
        domain = resourceBundle.getString("domain").trim();
        
            processPage(domain);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void processPage(String URL) throws IOException, SQLException, Exception {
        //check if the given URL is already in database 
        boolean ifExists = operationDao.checkIfDomainExists(URL);
        if (!ifExists) {
            //get useful information
            operationDao.insertRecord(URL);

            System.out.println(URL);
            Connection connection = Jsoup.connect(URL).userAgent(USER_AGENT);
            Document doc = connection.get();
            getImages(doc);
            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for (Element link : questions) {
                //check if the given URL is already in database 
                if (link.absUrl("href").contains(domain)) {
                    boolean ifExistsLink = operationDao.checkIfDomainExists(link.absUrl("href"));
                    if (!ifExistsLink) {
                        System.out.println(link.absUrl("href"));

                        operationDao.insertRecord(link.absUrl("href"));
                        processPage(link.absUrl("href"));
                    }
                }
            }
            operationDao.writeToFile();
        }
        
    }

    public static void getImages(Document doc) throws SQLException, Exception {
        Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
        for (Element image : images) {
            boolean ifExistsImage = operationDao.checkIfDomainExists(image.attr("src"));
            //store the image URL to database to avoid parsing again
            if (!ifExistsImage) {
                operationDao.insertRecord(image.attr("src"));
            }
        }
    }
}
