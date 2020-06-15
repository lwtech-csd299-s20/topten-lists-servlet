package edu.lwtech.csd299.topten;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

public class TopTenListSqlDAO implements DAO<TopTenList> {
    
    private static final Logger logger = Logger.getLogger(TopTenListSqlDAO.class.getName());

    private Connection conn = null;

    public TopTenListSqlDAO() {
        this.conn = null;                                   // conn must be created during init()
    }

    public boolean init() {
        logger.info("Connecting to the database...");

        String jdbcDriver = "org.mariadb.jdbc.Driver";      // The MariaDB driver works better than the MySQL driver
        // String url = "jdbc:mariadb://localhost:3306/topten?useSSL=false&allowPublicKeyRetrieval=true";
        String url = "jdbc:mariadb://csd299.cv18zcsjzteu.us-west-2.rds.amazonaws.com:3306/topten?useSSL=false&allowPublicKeyRetrieval=true";

        conn = SQLUtils.connect(url, "topten", "lwtech2000", jdbcDriver);       //TODO: Remove DB credentials from the source code!
        if (conn == null) {
            logger.error("Unable to connect to SQL Database with URL: " + url);
            return false;
        }
        logger.info("...connected!");

        return true;
    }

    public int insert(TopTenList list) {
        logger.debug("Inserting " + list + "...");

        if (list.getID() != -1) {
            logger.error("Attempting to add previously added TopTenList: " + list);
            return -1;
        }

        String query = "INSERT INTO TopTenLists";
        query += " (description, item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, published, ownerID, numViews, numLikes)";
        query += " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String description = list.getDescription();
        List<String> items = list.getItems();
        String isPublished = list.isPublished() ? "Y" : "N";
        String ownerID = "" + list.getOwnerID();
        String numViews = "" + list.getNumViews();
        String numLikes = "" + list.getNumLikes();

        int listID = SQLUtils.executeSQLInsert(conn, query, "listID", description,
            items.get(0), items.get(1), items.get(2), items.get(3), items.get(4),
            items.get(5), items.get(6), items.get(7), items.get(8), items.get(9),
            isPublished, ownerID, numViews, numLikes);    
        
        logger.debug("TopTenList successfully inserted with listID = " + listID);
        return listID;
    }

    public void delete(int listID) {
        logger.debug("Trying to delete TopTenList with index: " + listID);

        String query = "DELETE FROM TopTenLists WHERE listID=" + listID;
        SQLUtils.executeSQL(conn, query);
    }
    
    public TopTenList getByID(int listID) {
        logger.debug("Trying to get TopTenList with ID: " + listID);
        
        String query = "SELECT listID, description,";
        query += " item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, isPublished, ownerID, numViews, numLikes";
        query += " FROM TopTenLists WHERE listID=" + listID;

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows != null) {
            logger.debug("Found list!");
        } else {
            logger.debug("Did not find list.");
            return null;
        }
        
        SQLRow row = rows.get(0);
        TopTenList toptenlist = convertRowToList(row);
        return toptenlist;
    }
    
    public TopTenList getByIndex(int index) {
        logger.debug("Trying to get TopTenList with index: " + index);
        
        index++;                                    // SQL uses 1-based indexes

        if (index < 1)
            return null;

        String query = "SELECT listID, description,";
        query += " item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, isPublished, ownerID, numViews, numLikes";
        query += " FROM TopTenLists ORDER BY listID LIMIT " + index;

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("Did not find list.");
            return null;
        }
        
        SQLRow row = rows.get(rows.size()-1);
        TopTenList toptenlist = convertRowToList(row);
        return toptenlist;
    }
    
    public List<TopTenList> getAll() {
        logger.debug("Getting all TopTenLists...");
        
        String query = "SELECT listID, description,";
        query += " item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, isPublished, ownerID, numViews, numLikes";
        query += " FROM TopTenLists ORDER BY listID";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("No lists found!");
            return null;
        }
       
        List<TopTenList> lists = new ArrayList<>();
        for (SQLRow row : rows) {
            TopTenList toptenlist = convertRowToList(row);
            lists.add(toptenlist);
        }
        return lists;
    }
    
    public List<Integer> getAllIDs() {
        logger.debug("Getting List IDs...");

        String query = "SELECT listID FROM TopTenLists ORDER BY listID";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("No lists found!");
            return null;
        }
        
        List<Integer> listIDs = new ArrayList<>();
        for (SQLRow row : rows) {
            String value = row.getItem("listID");
            int i = Integer.parseInt(value);
            listIDs.add(i);
        }
        return listIDs;
    }

    @Override
    public TopTenList search(String keyword) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        logger.debug("Getting the number of rows...");

        String query = "SELECT count(*) FROM TopTenLists";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        if (rows == null) {
            logger.error("No lists found!");
            return 0;
        }

        String value = rows.get(0).getItem();
        return Integer.parseInt(value);
    }

    @Override
    public boolean update(TopTenList list) {
        logger.debug("Updating views and likes for list #" + list.getID());

        String query = "UPDATE TopTenLists " + 
                "SET numViews='" + list.getNumViews() + "', numLikes='" + list.getNumLikes() + "' " +
                "WHERE listID='" + list.getID() + "'";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);

        return (rows != null);
    }

    public void disconnect() {
        SQLUtils.disconnect(conn);
        conn = null;
    }
    

    // =====================================================================

    private TopTenList convertRowToList(SQLRow row) {
        List<String> items = new ArrayList<>();
        int listID = Integer.parseInt(row.getItem("listID"));
        String description = row.getItem("description");
        for (int i=10; i > 0; i--)                              // Insert items going from #10 down to #1 so that they print out correctly
            items.add(row.getItem("item"+i));
        boolean isPublished = row.getItem("isPublished").equalsIgnoreCase("Y");
        int ownerID = Integer.parseInt(row.getItem("ownerID"));
        long numViews = Long.parseLong(row.getItem("numViews"));
        long numLikes = Long.parseLong(row.getItem("numLikes"));
        return new TopTenList(listID, description, items, isPublished, ownerID, numViews, numLikes);
    }

}
