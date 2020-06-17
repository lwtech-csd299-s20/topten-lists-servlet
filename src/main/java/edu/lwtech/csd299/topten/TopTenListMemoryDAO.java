package edu.lwtech.csd299.topten;

import java.util.*;

import org.apache.log4j.Logger;

public class TopTenListMemoryDAO implements DAO<TopTenList> {
    
    private static final Logger logger = Logger.getLogger(TopTenListMemoryDAO.class.getName());
    
    private int nextID;
    private List<TopTenList> memoryDB;

    public TopTenListMemoryDAO() {
        this.nextID = 1000;
        this.memoryDB = new ArrayList<>();
    }

    public boolean init(String jdbc, String user, String password, String driver) {
        addDemoTopTenListData();
        return true;
    }

    public int insert(TopTenList list) {
        logger.debug("Inserting " + list + "...");

        if (list.getID() != -1) {
            logger.error("Attempting to add previously added list: " + list);
            return -1;
        }
        
        TopTenList newList = new TopTenList(generateNextID(), list);       // Replace -1 ID with official ID
        memoryDB.add(newList);
        
        logger.debug("Item successfully inserted!");
        return newList.getID();
    }
    
    public void delete(int id) {
        logger.debug("Trying to delete list with ID: " + id);

        TopTenList listFound = null;
        for (TopTenList list : memoryDB) {
            if (list.getID() == id) {
                listFound = list;
                break;
            }
        }
        if (listFound != null)
            memoryDB.remove(listFound);
    }

    public TopTenList getByID(int id) {
        logger.debug("Trying to get list with ID: " + id);
        
        TopTenList listFound = null;
        for (TopTenList list : memoryDB) {
            if (list.getID() == id) {
                listFound = list;
                break;
            }
        }
        return listFound;
    }
    
    public TopTenList getByIndex(int index) {
        // Note: indexes are zero-based
        logger.debug("Getting list with index: " + index);
        return memoryDB.get(index);
    }
    
    public List<TopTenList> getAll() {
        logger.debug("Getting all items");
        return new ArrayList<>(memoryDB);       // Return copy of List colleciton
    }    
    
    public List<Integer> getAllIDs() {
        logger.debug("Getting list IDs...");

        List<Integer> listIDs = new ArrayList<>();
        for (TopTenList list : memoryDB) {
            listIDs.add(list.getID());
        }
        return listIDs;
    }

    public List<TopTenList> search(String keyword) {
        logger.debug("Searching for lists containing: " + keyword);
        
        keyword = keyword.toLowerCase();
        List<TopTenList> lists = new ArrayList<>();
        for (TopTenList list : memoryDB) {
            boolean found = false;
            List<String> items = list.getItems();
            for (String item : items) {
                if (item.toLowerCase().contains(keyword)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                lists.add(list);
                break;
            }
        }
        return lists;
    }

    public int size() {
        return memoryDB.size();
    }

    public boolean update(TopTenList newList) {
        int id = newList.getID();
        logger.debug("Updating list (" + id + ") with " + newList);

        TopTenList oldList = getByID(id);
        if (oldList == null)
            return false;

        memoryDB.remove(oldList);
        memoryDB.add(newList);
        memoryDB.sort(Comparator.comparing(TopTenList::getID));
        return true;
    }

    public void disconnect() {
        memoryDB = null;
    }

    // =================================================================
    
    private synchronized int generateNextID() {
        return nextID++;
    }

    private void addDemoTopTenListData() {
        logger.debug("Creating demo TopTenLists...");

        String description;
        List<String> items;
    
        int owner = 1;

        description = "Top 10 Favorite Roman Numerals";
        items = Arrays.asList(
            "X",
            "IX",
            "VIII",
            "VII",
            "VI",
            "V",
            "IV",
            "III",
            "II",
            "I"
        );
        insert(new TopTenList(description, items, owner));

        description = "Top 10 Favorite Planets";
        items = Arrays.asList(
            "Hollywood",
            "Neptune",
            "Uranus",
            "Venus",
            "Mercury",
            "Earth",
            "Mars",
            "Jupiter",
            "Saturn",
            "Pluto!!!"
        );
        insert(new TopTenList(description, items, owner));
        
        description = "Top 10 Favorite Star Wars Movies";
        items = Arrays.asList(
            "III: Revenge of the Sith",
            "II: Attack of the Clones",
            "VIII: The Last Jedi",
            "IX: The Rise of Skywalker",
            "VI: Return of the Jedi",
            "I: The Phantom Menace",
            "VII: The Force Awakens",
            "The Mandelorian Compilation",
            "IV: A New Hope",
            "V: The Empire Strikes Back"
        );
        insert(new TopTenList(description, items, owner));
        
        logger.info(size() + " lists inserted");
    }

}
