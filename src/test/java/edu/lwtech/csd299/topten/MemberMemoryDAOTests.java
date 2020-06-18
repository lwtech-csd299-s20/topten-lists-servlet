package edu.lwtech.csd299.topten;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MemberMemoryDAOTests {

    private DAO<Member> memoryDAO;

    private Member fred;

    @Before
    public void setUp() {
        fred = new Member("fred@lwtech.edu", "12345678");

        memoryDAO = new MemberMemoryDAO();
        memoryDAO.init("jdbc","username","password","driver");  // Params ignored for memory DAO
    }

    @After
    public void tearDown() {
        memoryDAO.disconnect();
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

    @Test(expected = UnsupportedOperationException.class)
    public void updateTest() {
        memoryDAO.update(new Member("A", "B"));
        fail();
    }

}
