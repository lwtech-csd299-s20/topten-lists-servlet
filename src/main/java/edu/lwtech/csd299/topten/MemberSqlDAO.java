package edu.lwtech.csd299.topten;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

public class MemberSqlDAO implements DAO<Member> {
    
    private static final Logger logger = Logger.getLogger(MemberSqlDAO.class.getName());

    private Connection conn = null;

    public MemberSqlDAO() {
        this.conn = null;                                   // conn must be created during init()
    }

    public boolean init() {
        logger.info("Connecting to the database...");

        String jdbcDriver = "org.mariadb.jdbc.Driver";      // The MariaDB driver works better than the MySQL driver
        String url = "jdbc:mariadb://localhost:3306/topten?useSSL=false&allowPublicKeyRetrieval=true";
        // String url = "jdbc:mariadb://csd299.cv18zcsjzteu.us-west-2.rds.amazonaws.com:3306/topten?useSSL=false&allowPublicKeyRetrieval=true";

        conn = SQLUtils.connect(url, "topten", "lwtech2000", jdbcDriver);       //TODO: Remove DB credentials from the source code!
        if (conn == null) {
            logger.error("Unable to connect to SQL Database with URL: " + url);
            return false;
        }
        logger.info("...connected!");

        return true;
    }

    public int insert(Member member) {
        logger.debug("Inserting " + member + "...");

        if (member.getID() != -1) {
            logger.error("Attempting to add previously added Member: " + member);
            return -1;
        }

        String query = "INSERT INTO Members (email, password) VALUES (?,?)";

        int memberID = SQLUtils.executeSQLInsert(conn, query, "memberID",
                member.getEmail(), member.getPassword());    
        
        logger.debug("Member successfully inserted with memberID = " + memberID);
        return memberID;
    }

    public void delete(int memberID) {
        logger.debug("Trying to delete Member with index: " + memberID);

        String query = "DELETE FROM Members WHERE memberID=" + memberID;
        SQLUtils.executeSQL(conn, query);
    }
    
    public Member getByID(int memberID) {
        logger.debug("Trying to get Member with ID: " + memberID);
        
        String query = "SELECT memberID, email, password";
        query += " FROM Members WHERE memberID=" + memberID;

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows != null) {
            logger.debug("Found member!");
        } else {
            logger.debug("Did not find member.");
            return null;
        }
        
        SQLRow row = rows.get(0);
        Member member = convertRowToList(row);
        return member;
    }
    
    public Member getByIndex(int index) {
        logger.debug("Trying to get Member with index: " + index);
        
        index++;                                    // SQL uses 1-based indexes

        if (index < 1)
            return null;

        String query = "SELECT memberID, email, password";
        query += " FROM Members ORDER BY memberID LIMIT " + index;

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("Did not find member.");
            return null;
        }
        
        SQLRow row = rows.get(rows.size()-1);
        Member member = convertRowToList(row);
        return member;
    }
    
    public List<Member> getAll() {
        logger.debug("Getting all Members...");
        
        String query = "SELECT memberID, email, password";
        query += " FROM Members ORDER BY memberID";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("No members found!");
            return null;
        }
       
        List<Member> members = new ArrayList<>();
        for (SQLRow row : rows) {
            Member member = convertRowToList(row);
            members.add(member);
        }
        return members;
    }
    
    public List<Integer> getAllIDs() {
        logger.debug("Getting all Member IDs...");

        String query = "SELECT memberID FROM Members ORDER BY memberID";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("No members found!");
            return null;
        }
        
        List<Integer> memberIDs = new ArrayList<>();
        for (SQLRow row : rows) {
            String value = row.getItem("memberID");
            int i = Integer.parseInt(value);
            memberIDs.add(i);
        }
        return memberIDs;
    }

    public List<Member> search(String keyword) {
        logger.debug("Searching for member with '" + keyword + "'");

        String query = "SELECT memberID FROM Members WHERE";
        query += " email like '%" + keyword + "%'";
        query += " ORDER BY memberID";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        
        if (rows == null) {
            logger.debug("No members found!");
            return null;
        }
       
        List<Member> members = new ArrayList<>();
        for (SQLRow row : rows) {
            Member member = convertRowToList(row);
            members.add(member);
        }
        return members;
    }

    public int size() {
        logger.debug("Getting the number of rows...");

        String query = "SELECT count(*) FROM Members";

        List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
        if (rows == null) {
            logger.error("No members found!");
            return 0;
        }

        String value = rows.get(0).getItem();
        return Integer.parseInt(value);
    }

    public boolean update(Member member) {
        throw new UnsupportedOperationException("Unable to update existing member in database.");
    }

    public void disconnect() {
        SQLUtils.disconnect(conn);
        conn = null;
    }
    

    // =====================================================================

    private Member convertRowToList(SQLRow row) {
        int memberID = Integer.parseInt(row.getItem("memberID"));
        String email = row.getItem("email");
        String password = row.getItem("password");
        return new Member(memberID, email, password);
    }

}
