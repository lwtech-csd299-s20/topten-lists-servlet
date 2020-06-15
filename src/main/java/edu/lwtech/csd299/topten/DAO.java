package edu.lwtech.csd299.topten;

import java.util.*;

public interface DAO<T> {
    public boolean init(String jdbc, String user, String password, String driver);
    public int insert(T item);
    public void delete(int id);
    public T getByID(int id);
    public T getByIndex(int index);
    public List<T> getAll();
    public List<Integer> getAllIDs();
    public List<T> search(String keyword);
    public int size();
    public boolean update(T item);
    public void disconnect();
}
