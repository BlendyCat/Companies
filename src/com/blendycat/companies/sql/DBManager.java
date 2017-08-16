package com.blendycat.companies.sql;

import com.blendycat.companies.Companies;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Created by EvanMerz on 8/6/17.
 */
public class DBManager {

    private String url;

    private Connection connection = null;
    private Properties cp = new Properties();
    private Companies main;

    /**
     * Constructor
     * @param main: main class of plugin
     */
    public DBManager(Companies main){
        FileConfiguration config = main.getConfig();
        if(!config.isSet("db_password")
                || !config.isSet("db_username")
                || !config.isSet("db_host")
                || !config.isSet("db_name")
                || config.get("db_password").equals("")
                || config.get("db_username").equals("")
                || config.get("db_host").equals("")
                || config.get("db_name").equals("")) {
            main.getLogger().info("Database login is not configured! Disabling Companies 1.0");
            main.getPluginLoader().disablePlugin(main);
        }

        //
        String dbUsername = config.getString("db_username");
        String dbPassword = config.getString("db_password");
        String dbHost = config.getString("db_host");
        String dbName = config.getString("db_name");

        //sets the property's username
        cp.put("username", dbUsername);
        //sets the property's password
        cp.put("password", dbPassword);
        //initialize the DB connect URL
        url = "jdbc:mysql://"+dbHost+"/"+dbName;
    }


    /**
     * Starts the connection to the db
     */
    public void startConnection(){
        try {
            connection = DriverManager.getConnection(url, cp);
            main.getLogger().info("Connected to database successfully!");
        }
        catch(SQLException ex){
            main.getLogger().info("Failed to connect to database");
        }
    }

    public void createTables(){
        try{
        CallableStatement createCompaniesTable = connection.prepareCall(
                "IF NOT EXISTS (select * from sys.tables where name='Companies')" +
                        "CREATE TABLE Companies (" +
                        "ID int NOT NULL," +
                        "CompanyName VARCHAR(255) NOT NULL," +
                        "CompanyType ENUM('goods','contractor') NOT NULL," +
                        "CompanyOwner VARCHAR(255) NOT NULL," +
                        "PRIMARY KEY(ID)," +
                        "UNIQUE(CompanyName)) ENGINE = MyISAM;");
        createCompaniesTable.execute();
        main.getLogger().info("created table!");
        }catch(SQLException ex){
            main.getLogger().severe(ex.getMessage());
        }
    }

    public void createCompany(String companyName, String owner){
        //TODO
    }

    public void addEmployee(String companyName, String employeeName, String positionTitle){
        //TODO en español?
    }


}
