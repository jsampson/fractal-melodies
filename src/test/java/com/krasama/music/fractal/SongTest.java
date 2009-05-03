package com.krasama.music.fractal;

import java.util.Collections;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import junit.framework.TestCase;

public class SongTest extends TestCase
{
    public void testSimpleSequence() throws Exception
    {
        Song song = new Song(Scale.C_MAJOR, new Note[] { Note.valueOf("0 1/2"), Note.valueOf("1 1/2") }, Note.valueOf("0 2"),
                Fraction.HALF, Fraction.valueOf(4), Collections.<Note> emptyList());
        Sequence sequence = song.sequence();
        assertEquals(500, sequence.getResolution());
        assertEquals(8000, sequence.getTickLength());
        Track[] tracks = sequence.getTracks();
        assertEquals(1, tracks.length);
        Track track = tracks[0];
        assertEquals(13, track.size());
        assertEquals(8000, track.ticks());
        assertNoteOn(0, 60, 0, track.get(0));
        assertNoteOff(0, 60, 1000, track.get(1));
        assertNoteOn(0, 62, 1000, track.get(2));
        assertNoteOff(0, 62, 2000, track.get(3));
        assertNoteOn(0, 60, 3000, track.get(4));
        assertNoteOff(0, 60, 4000, track.get(5));
        assertNoteOn(0, 62, 4000, track.get(6));
        assertNoteOff(0, 62, 5000, track.get(7));
        assertNoteOn(0, 62, 5000, track.get(8));
        assertNoteOff(0, 62, 6000, track.get(9));
        assertNoteOn(0, 64, 6000, track.get(10));
        assertNoteOff(0, 64, 8000, track.get(11));
        assertEndOfTrack(8000, track.get(12));
    }
    
    public void testNoteValidation() throws Exception
    {
        Song song = new Song(Scale.C_MAJOR, new Note[] { Note.valueOf("-20 1/2"), Note.valueOf("20 1/2") }, Note.valueOf("0 4"),
                Fraction.HALF, Fraction.valueOf(4), Collections.<Note> emptyList());
        try
        {
            song.sequence();
            fail();
        }
        catch (SongException expected)
        {
            assertEquals("Notes in this song exceed the physical range of a standard piano.", expected.getMessage());
        }        
    }

    private void assertNoteOn(int channel, int noteNumber, int timestamp, MidiEvent event)
    {
        assertShortMessage(ShortMessage.NOTE_ON, channel, noteNumber, timestamp, event);
    }

    private void assertNoteOff(int channel, int noteNumber, int timestamp, MidiEvent event)
    {
        assertShortMessage(ShortMessage.NOTE_OFF, channel, noteNumber, timestamp, event);
    }

    private void assertShortMessage(int command, int channel, int data1, int timestamp, MidiEvent event)
    {
        ShortMessage message = (ShortMessage) event.getMessage();
        assertEquals(command, message.getCommand());
        assertEquals(channel, message.getChannel());
        assertEquals(data1, message.getData1());
        assertEquals(timestamp, event.getTick());
    }

    private void assertEndOfTrack(int timestamp, MidiEvent event)
    {
        MetaMessage message = (MetaMessage) event.getMessage();
        assertEquals(47, message.getType());
        assertEquals(timestamp, event.getTick());
    }
}
