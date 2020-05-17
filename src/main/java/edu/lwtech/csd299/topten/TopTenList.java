package edu.lwtech.csd299.topten;

import java.util.*;

public class TopTenList {
    
    // Encapsulated member variables
    private int id;                 // Database ID (or -1 if it isn't in the database yet)
    private String description;
    private List<String> items;
    private int ownerID;
    private boolean published;
    private long numViews;
    private long numLikes;
    
    public TopTenList(int id, TopTenList list) {
        this(id, list.description, list.items, list.published, list.ownerID);
    }

    public TopTenList(String description, List<String> items, int ownerID) {
        this(-1, description, items, false, ownerID);
    }
    
    public TopTenList(int id, String description, List<String> items, boolean published, int ownerID) {

        if (id < -1) throw new IllegalArgumentException("Invalid TopTenList argument: id < -1");
        if (description == null) throw new IllegalArgumentException("Invalid TopTenList argument: description is null");
        if (description == "") throw new IllegalArgumentException("Invalid TopTenList argument: description is empty");
        if (items == null) throw new IllegalArgumentException("Invalid TopTenList argument: item list is null");
        if (items.size() < 10) throw new IllegalArgumentException("Invalid TopTenList argument: less than 10 items");
        if (ownerID < 0) throw new IllegalArgumentException("Invalid TopTenList argument: ownerID < 0");

        this.id = id;
        this.description = description;
        this.items = items;
        this.ownerID = ownerID;
        this.published = published;
        this.numViews = 0;
        this.numLikes = 0;
    }
    
    public int getID() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getItems() {
        return new ArrayList<>(items);      // Return a copy of the item list
    }

    public int getOwnerID() {
        return this.ownerID;
    }

    public boolean isPublished() {
        return this.published;
    }

    public long getNumViews() {
        return this.numViews;
    }

    public long getNumLikes() {
        return this.numLikes;
    }

    public long addView() {
        return ++numViews;
    }

    public long addLike() {
        return ++numLikes;
    }

    public void publish(int ownerID) {
        if (ownerID == this.ownerID)
            published = true;
    }
    
    @Override
    public String toString() {
        return id + ": " + description
                + " (" + numViews + "/" + numLikes + ") "
                + (published ? "[Published]" : "[Draft]");
    }

}
