package edu.lwtech.csd299.topten;

import org.junit.*;
import static org.junit.Assert.*;

public class MemberTests {

    Member fred;
    Member tom;
    Member mary;

    @Before
    public void setUp() {
        fred = new Member("fred@lwtech.edu", "12345678");
        tom = new Member("tom@lwtech.edu", "12345678");
        mary = new Member("mary@lwtech.edu", "12345678");
    }

    @Test
    public void getIdTest() {
        assertEquals(-1, fred.getID());
        assertEquals(-1, tom.getID());
        assertEquals(-1, mary.getID());
    }

    @Test
    public void getEmailTest() {
        assertTrue(fred.getEmail().contains("fred"));
        assertTrue(tom.getEmail().contains("tom"));
        assertTrue(mary.getEmail().contains("mary"));
    }

    @Test
    public void getPasswordTest() {
        assertTrue(fred.getPassword().startsWith("123"));
        assertTrue(tom.getPassword().startsWith("123"));
        assertTrue(mary.getPassword().startsWith("123"));
    }

    @Test
    public void toStringTest() {
        assertTrue(fred.toString().startsWith("["));
        assertTrue(tom.toString().contains("***"));
        assertTrue(mary.toString().endsWith("]"));
    }

}
