package edu.lwtech.csd299.topten;

import java.util.*;

public interface DAO<Pojo> {
    public boolean init();
    public int insert(Pojo pojo);
    public void delete(int id);
    public Pojo getByID(int id);
    public Pojo getByIndex(int index);
    public List<Pojo> getAll();
    public List<Integer> getAllIDs();
    public Pojo search(String keyword);
    public int size();
    public void disconnect();
}
