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

    public boolean init() {
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

    public Member search(String keyword) {
        logger.debug("Trying to get member with email address containing: " + keyword);
        
        keyword = keyword.toLowerCase();
        Member memberFound = null;
        for (Member member : memoryDB) {
            if (member.getEmail().toLowerCase().contains(keyword)) {
                memberFound = member;
                break;
            }
        }
        return memberFound;
    }

    public int size() {
        return memoryDB.size();
    }

    public boolean update(Member member) {
        return false;
    }

    public void disconnect() {
        memoryDB = null;
    }

    // =================================================================
    
    public synchronized int generateNextID() {
        return nextID++;
    }
}
