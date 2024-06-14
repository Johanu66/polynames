package database;

import java.sql.SQLException;

public class PolynameDatabase extends MySQLDatabase{
    static private PolynameDatabase instance = null;
    public PolynameDatabase() throws SQLException{
        super("localhost", 3306, "polynames", "root", "");
    }


    static public PolynameDatabase getInstance() throws Exception{
        if(PolynameDatabase.instance==null){
            PolynameDatabase.instance = new PolynameDatabase();
        }

        return PolynameDatabase.instance;
    }
}
