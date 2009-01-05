package com.krasama.music.fractal;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class NoteTest extends TestCase
{
    public void testMajorScale()
    {
        Scale scale = Scale.C_MAJOR;
        assertEquals(48, new Note(-7, null).getNoteNumber(scale));
        assertEquals(50, new Note(-6, null).getNoteNumber(scale));
        assertEquals(52, new Note(-5, null).getNoteNumber(scale));
        assertEquals(53, new Note(-4, null).getNoteNumber(scale));
        assertEquals(55, new Note(-3, null).getNoteNumber(scale));
        assertEquals(57, new Note(-2, null).getNoteNumber(scale));
        assertEquals(59, new Note(-1, null).getNoteNumber(scale));
        assertEquals(60, new Note(0, null).getNoteNumber(scale));
        assertEquals(62, new Note(1, null).getNoteNumber(scale));
        assertEquals(64, new Note(2, null).getNoteNumber(scale));
        assertEquals(65, new Note(3, null).getNoteNumber(scale));
        assertEquals(67, new Note(4, null).getNoteNumber(scale));
        assertEquals(69, new Note(5, null).getNoteNumber(scale));
        assertEquals(71, new Note(6, null).getNoteNumber(scale));
        assertEquals(72, new Note(7, null).getNoteNumber(scale));
    }

    public void testMarchTime()
    {
        Fraction ticksPerMeasure = Fraction.valueOf(2000);
        assertEquals(2000L, new Note(0, Fraction.valueOf("1")).getTicks(ticksPerMeasure));
        assertEquals(4000L, new Note(0, Fraction.valueOf("2")).getTicks(ticksPerMeasure));
        assertEquals(1000L, new Note(0, Fraction.valueOf("1/2")).getTicks(ticksPerMeasure));
        assertEquals(667L, new Note(0, Fraction.valueOf("1/3")).getTicks(ticksPerMeasure));
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

    public void testReverse()
    {
        // +0 ->
        // -0 +1 ->
        // -1 +0 -1 +2 ->
        // -2 +1 -0 +1 -2 +1 -2 +3 ->
        // -3 +2 -1 +2 -1 +0 -1 +2 -3 +2 -1 +2 -3 +2 -3 +4
        Note[] pattern = { new Note(0, Fraction.valueOf("1/2"), true), new Note(1, Fraction.valueOf("1/2"), false) };
        Note start = new Note(0, Fraction.valueOf("2"));
        Fraction eighth = Fraction.valueOf("1/8");
        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, eighth);
        assertEquals(16, result.size());
        assertEquals(3, result.get(0).pitch);
        assertEquals(eighth, result.get(0).length);
        assertEquals(2, result.get(1).pitch);
        assertEquals(eighth, result.get(1).length);
        assertEquals(1, result.get(2).pitch);
        assertEquals(eighth, result.get(2).length);
        assertEquals(2, result.get(3).pitch);
        assertEquals(eighth, result.get(3).length);
        assertEquals(1, result.get(4).pitch);
        assertEquals(eighth, result.get(4).length);
        assertEquals(0, result.get(5).pitch);
        assertEquals(eighth, result.get(5).length);
        assertEquals(1, result.get(6).pitch);
        assertEquals(eighth, result.get(6).length);
        assertEquals(2, result.get(7).pitch);
        assertEquals(eighth, result.get(7).length);
        assertEquals(3, result.get(8).pitch);
        assertEquals(eighth, result.get(8).length);
        assertEquals(2, result.get(9).pitch);
        assertEquals(eighth, result.get(9).length);
        assertEquals(1, result.get(10).pitch);
        assertEquals(eighth, result.get(10).length);
        assertEquals(2, result.get(11).pitch);
        assertEquals(eighth, result.get(11).length);
        assertEquals(3, result.get(12).pitch);
        assertEquals(eighth, result.get(12).length);
        assertEquals(2, result.get(13).pitch);
        assertEquals(eighth, result.get(13).length);
        assertEquals(3, result.get(14).pitch);
        assertEquals(eighth, result.get(14).length);
        assertEquals(4, result.get(15).pitch);
        assertEquals(eighth, result.get(15).length);
    }
}
