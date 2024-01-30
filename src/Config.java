import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Config {

    private static String url;
    private static String username;
    private static String password;

    static{

        Properties prop = new Properties();
        try(InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")){
            // Load the properties file
            if(input == null)
            {
                System.out.println("property file is not found");
            }
            prop.load(input);

            //Get properties
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Connection connectDB() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }
}
