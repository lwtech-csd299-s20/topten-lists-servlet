package edu.lwtech.csd299.topten;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MemberMemoryDAOTests {

    private DAO<Member> memoryDAO;

    private Member fred;
    private Member tom;
    private Member mary;

    public MemberMemoryDAOTests() {
        fred = new Member("fred@lwtech.edu", "12345678");
        tom = new Member("tom@lwtech.edu", "12345678");
        mary = new Member("mary@lwtech.edu", "12345678");

        memoryDAO = new MemberMemoryDAO();
        memoryDAO.insert(fred);
        memoryDAO.insert(tom);
        memoryDAO.insert(mary);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void insertTest() {
        assertEquals(3, memoryDAO.size());
        int memberID = memoryDAO.insert(fred);        // Add a second instance of fred
        assertEquals(4, memoryDAO.size());
        assertTrue(memberID > 0);
    }
    
    @Test
    public void getByIDTest() {
        Member member = memoryDAO.getByID(1000);
        assertEquals(1000, member.getID());
        member = memoryDAO.getByID(1001);
        assertEquals(1001, member.getID());
    }
    
    @Test
    public void getByIndexTest() {
        Member member = memoryDAO.getByIndex(0);
        assertEquals(1000, member.getID());
        member = memoryDAO.getByIndex(1);
        assertEquals(1001, member.getID());
    }
    
    @Test
    public void getAllTest() {
        List<Member> allLists = new ArrayList<>();
        allLists = memoryDAO.getAll();
        assertEquals(3, allLists.size());
    }
    
}
