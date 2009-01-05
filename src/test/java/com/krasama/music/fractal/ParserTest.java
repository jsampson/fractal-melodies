package com.krasama.music.fractal;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import junit.framework.TestCase;

public class ParserTest extends TestCase
{
    public void testNormalizeLine()
    {
        assertEquals(null, Parser.normalizeLine(""));
        assertEquals(null, Parser.normalizeLine("   "));
        assertEquals("foo", Parser.normalizeLine("foo"));
        assertEquals("foo", Parser.normalizeLine("   foo   "));
        assertEquals("foo bar ack", Parser.normalizeLine("   foo \t bar    ack \r\n"));
        assertEquals("foo:bar,ack", Parser.normalizeLine("   foo : \t bar  ,  ack \r\n"));
        assertEquals("notes:0 1/2,-2 -1/4,1 1/4", Parser.normalizeLine("    Notes: 0 1/2, -2 -1/4, 1 1/4\n"));
        assertEquals("foo:bar,ack", Parser.normalizeLine("   foo : \t bar  ,  ack // howdy\r\n"));
        assertEquals(null, Parser.normalizeLine("  // howdy \r\n"));
    }

    public void testParseSongs() throws Exception
    {
        InputStream file = this.getClass().getResourceAsStream("/test-songs.txt");
        Map<String, Song> songs = Parser.parseSongs(file);
        assertEquals(2, songs.size());
        assertTrue(songs.containsKey("first song"));
        assertTrue(songs.containsKey("second song"));
        Song firstSong = songs.get("first song");
        assertEquals(new Scale(60, 2, 4, 5, 7, 9, 11, 12), firstSong.scale);
        assertEquals(new Note[] { new Note(-2, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")),
                new Note(1, Fraction.valueOf("1/2"), true) }, firstSong.pattern);
        assertEquals(new Note(0, Fraction.valueOf(32)), firstSong.start);
        assertEquals(Fraction.valueOf("1/8"), firstSong.bottom);
        assertEquals(Fraction.valueOf(4), firstSong.beats);
        assertNull(firstSong.harmonyStart);
        assertNull(firstSong.harmonyBottom);
        Song secondSong = songs.get("second song");
        assertEquals(new Scale(57, 2, 3, 5, 7, 8, 10, 12), secondSong.scale);
        assertEquals(new Note[] { new Note(-2, Fraction.valueOf("1/3")), new Note(1, Fraction.valueOf("1/3"), true),
                new Note(0, Fraction.valueOf("1/3")) }, secondSong.pattern);
        assertEquals(new Note(0, Fraction.valueOf(9)), secondSong.start);
        assertEquals(Fraction.valueOf("1/9"), secondSong.bottom);
        assertEquals(Fraction.valueOf(3), secondSong.beats);
        assertEquals(new Note(-7, Fraction.valueOf(9)), secondSong.harmonyStart);
        assertEquals(Fraction.valueOf("1/3"), secondSong.harmonyBottom);
    }

    private void assertEquals(Note[] expected, Note[] actual)
    {
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));
    }
}
