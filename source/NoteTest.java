import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class NoteTest extends TestCase
{
    public void testMajorScale()
    {
        assertEquals(48, new Note(-7, null).getNumber());
        assertEquals(50, new Note(-6, null).getNumber());
        assertEquals(52, new Note(-5, null).getNumber());
        assertEquals(53, new Note(-4, null).getNumber());
        assertEquals(55, new Note(-3, null).getNumber());
        assertEquals(57, new Note(-2, null).getNumber());
        assertEquals(59, new Note(-1, null).getNumber());
        assertEquals(60, new Note(0, null).getNumber());
        assertEquals(62, new Note(1, null).getNumber());
        assertEquals(64, new Note(2, null).getNumber());
        assertEquals(65, new Note(3, null).getNumber());
        assertEquals(67, new Note(4, null).getNumber());
        assertEquals(69, new Note(5, null).getNumber());
        assertEquals(71, new Note(6, null).getNumber());
        assertEquals(72, new Note(7, null).getNumber());
    }

    public void testMarchTime()
    {
        assertEquals(2000L, new Note(0, Fraction.valueOf("1")).getTime());
        assertEquals(4000L, new Note(0, Fraction.valueOf("2")).getTime());
        assertEquals(1000L, new Note(0, Fraction.valueOf("1/2")).getTime());
        assertEquals(666L, new Note(0, Fraction.valueOf("1/3")).getTime());
    }

    public void testIterate()
    {
        Note[] pattern = { new Note(0, Fraction.valueOf("1/2")), new Note(1, Fraction.valueOf("1/2")) };
        Note start = new Note(0, Fraction.valueOf("2"));
        Fraction eighth = Fraction.valueOf("1/8");
        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, eighth);
        assertEquals(16, result.size());
        assertEquals(0, result.get(0).pitch);
        assertEquals(eighth, result.get(0).length);
        assertEquals(1, result.get(1).pitch);
        assertEquals(eighth, result.get(1).length);
        assertEquals(1, result.get(2).pitch);
        assertEquals(eighth, result.get(2).length);
        assertEquals(2, result.get(3).pitch);
        assertEquals(eighth, result.get(3).length);
        assertEquals(1, result.get(4).pitch);
        assertEquals(eighth, result.get(4).length);
        assertEquals(2, result.get(5).pitch);
        assertEquals(eighth, result.get(5).length);
        assertEquals(2, result.get(6).pitch);
        assertEquals(eighth, result.get(6).length);
        assertEquals(3, result.get(7).pitch);
        assertEquals(eighth, result.get(7).length);
        assertEquals(1, result.get(8).pitch);
        assertEquals(eighth, result.get(8).length);
        assertEquals(2, result.get(9).pitch);
        assertEquals(eighth, result.get(9).length);
        assertEquals(2, result.get(10).pitch);
        assertEquals(eighth, result.get(10).length);
        assertEquals(3, result.get(11).pitch);
        assertEquals(eighth, result.get(11).length);
        assertEquals(2, result.get(12).pitch);
        assertEquals(eighth, result.get(12).length);
        assertEquals(3, result.get(13).pitch);
        assertEquals(eighth, result.get(13).length);
        assertEquals(3, result.get(14).pitch);
        assertEquals(eighth, result.get(14).length);
        assertEquals(4, result.get(15).pitch);
        assertEquals(eighth, result.get(15).length);
    }
}
