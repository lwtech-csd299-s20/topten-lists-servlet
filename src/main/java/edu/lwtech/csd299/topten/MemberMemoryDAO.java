package edu.lwtech.csd299.topten;

import java.util.*;

import org.apache.log4j.Logger;

public class MemberMemoryDAO implements DAO<Member> {
    
    private static final Logger logger = Logger.getLogger(MemberMemoryDAO.class.getName());
    
    private int nextID;
    private List<Member> memoryDB;

    public MemberMemoryDAO() {
        this.nextID = 1000;
        this.memoryDB = new ArrayList<>();
    }

    public boolean init(String jdbc, String user, String password, String driver) {
        addDemoMemberData();
        return true;
    }

    public int insert(Member member) {
        logger.debug("Inserting " + member + "...");

        if (member.getID() != -1) {
            logger.error("Attempting to add previously added member: " + member);
            return -1;
        }
        
        Member newMember = new Member(generateNextID(), member);       // Replace -1 ID with official ID
        memoryDB.add(newMember);
        
        logger.debug("Member successfully inserted!");
        return newMember.getID();
    }
    
    public void delete(int id) {
        logger.debug("Trying to delete member with ID: " + id);

        Member memberFound = null;
        for (Member member : memoryDB) {
            if (member.getID() == id) {
                memberFound = member;
                break;
            }
        }
        if (memberFound != null)
            memoryDB.remove(memberFound);
    }

    public Member getByID(int id) {
        logger.debug("Trying to get member with ID: " + id);
        
        Member memberFound = null;
        for (Member member : memoryDB) {
            if (member.getID() == id) {
                memberFound = member;
                break;
            }
        }
        return memberFound;
    }
    
    public Member getByIndex(int index) {
        // Note: indexes are zero-based
        logger.debug("Getting member with index: " + index);
        return memoryDB.get(index);
    }
    
    public List<Member> getAll() {
        logger.debug("Getting all items");
        return new ArrayList<>(memoryDB);       // Return copy of Member colleciton
    }    
    
    public List<Integer> getAllIDs() {
        logger.debug("Getting member IDs...");

        List<Integer> memberIDs = new ArrayList<>();
        for (Member member : memoryDB) {
            memberIDs.add(member.getID());
        }
        return memberIDs;
    }

    public List<Member> search(String keyword) {
        logger.debug("Trying to get member with email address containing: " + keyword);
        
        keyword = keyword.toLowerCase();
        List<Member> membersFound = new ArrayList<>();
        for (Member member : memoryDB) {
            if (member.getEmail().toLowerCase().contains(keyword)) {
                membersFound.add(member);
                break;
            }
        }
        return membersFound;
    }

    public int size() {
        return memoryDB.size();
    }

    public boolean update(Member member) {
        throw new UnsupportedOperationException("Unable to update existing member in database.");
    }

    public void disconnect() {
        memoryDB = null;
    }

    // =================================================================
    
    private synchronized int generateNextID() {
        return nextID++;
    }

    private void addDemoMemberData() {
        logger.debug("Creating demo Members...");

        Member member;
        member = new Member("fred@lwtech.edu", "12345678");
        insert(member);
        member = new Member("tom@lwtech.edu", "12345678");
        insert(member);
        member = new Member("mary@lwtech.edu", "12345678");
        insert(member);

        logger.info(size() + " members inserted");
    }

}
