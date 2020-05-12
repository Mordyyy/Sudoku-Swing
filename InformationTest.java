import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformationTest {
    @Test
    public void testDatabase(){
        Information dat = new Information("Kutaisi", "Europe", 200000);
        assertEquals("Kutaisi", dat.getMetropolis());
        assertEquals("Europe", dat.getContinent());
        assertEquals(200000, dat.getPopulation());
    }

}