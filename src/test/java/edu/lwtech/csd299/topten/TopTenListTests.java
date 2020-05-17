package edu.lwtech.csd299.topten;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TopTenListTests {

    TopTenList romanList;
    TopTenList planetsList;
    TopTenList starWarsList;

    public TopTenListTests() {
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
        romanList = new TopTenList(description, items, owner);

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
        planetsList = new TopTenList(description, items, owner);
        
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
        starWarsList = new TopTenList(description, items, owner);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void getIdTest() {
        assertEquals(-1, romanList.getID());
        assertEquals(-1, planetsList.getID());
        assertEquals(-1, starWarsList.getID());
    }

    @Test
    public void getTitleTest() {
        assertEquals("Top 10 Favorite Roman Numerals", romanList.getDescription());
        assertEquals("Top 10 Favorite Planets", planetsList.getDescription());
        assertEquals("Top 10 Favorite Star Wars Movies", starWarsList.getDescription());
    }

    @Test
    public void publishTest() {
        assertFalse(romanList.isPublished());
        assertFalse(planetsList.isPublished());
        starWarsList.publish(1);
        assertTrue(starWarsList.isPublished());
    }

    @Test
    public void toStringTest() {
        assertTrue(romanList.toString().contains("Top 10"));
        assertTrue(romanList.toString().contains("Draft"));
        assertTrue(planetsList.toString().contains("Top 10"));
        assertTrue(planetsList.toString().contains("Draft"));
        starWarsList.publish(1);
        assertTrue(starWarsList.toString().contains("Top 10"));
        assertTrue(starWarsList.toString().contains("Published"));
    }

}
