package com.krasama.music.fractal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Note[] pattern = { new Note(-1, Fraction.valueOf("1/4")), new Note(0,
        // Fraction.valueOf("1/2")), new Note(1, Fraction.valueOf("1/4")) };
        // Note start = new Note(0, Fraction.valueOf("4"));
        // Fraction bottom = Fraction.valueOf("1/8");
        Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")),
                new Note(1, Fraction.valueOf("1/2")) };
        Note start = new Note(0, Fraction.valueOf("4"));
        Fraction bottom = Fraction.valueOf("1/8");
        // Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0,
        // Fraction.valueOf("1/4")),
        // new Note(1, Fraction.valueOf("1/2"), true) };
        // Note start = new Note(0, Fraction.valueOf("128"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(-2, Fraction.valueOf("1/2")), new Note(3,
        // Fraction.valueOf("1/2")) };
        // Note start = new Note(0, Fraction.valueOf("16"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(-1, Fraction.valueOf("1/4")), new Note(0,
        // Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/4")), new
        // Note(0, Fraction.valueOf("1/4")) };
        // Note start = new Note(0, Fraction.valueOf("64"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(0, Fraction.valueOf("1/4")), new Note(1,
        // Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/2")) };
        // Note start = new Note(0, Fraction.valueOf("32"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(0, Fraction.valueOf("1/4")), new Note(1,
        // Fraction.valueOf("1/4")), new Note(-1, Fraction.valueOf("1/4")), new
        // Note(0, Fraction.valueOf("1/4")) };
        // Note start = new Note(0, Fraction.valueOf("64"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(0, Fraction.valueOf("1/8")), new Note(-1,
        // Fraction.valueOf("1/8")), new Note(0, Fraction.valueOf("3/4")) };
        // Note start = new Note(0, Fraction.valueOf("32"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note[] pattern = { new Note(0, Fraction.valueOf("1/8")), new Note(-3,
        // Fraction.valueOf("1/8")), new Note(0, Fraction.valueOf("3/4")) };
        // Note start = new Note(0, Fraction.valueOf("32"));
        // Fraction bottom = Fraction.valueOf("1/16");
        // Note start = new Note(0, Fraction.valueOf("1"));
        // Fraction bottom = Fraction.valueOf("1/4");
        // Note[] pattern = { new Note(2, Fraction.valueOf("1/6")), new Note(1,
        // Fraction.valueOf("1/6")), new Note(0, Fraction.valueOf("1/6")), new
        // Note(3, Fraction.valueOf("1/6")), new Note(3,
        // Fraction.valueOf("1/6")), new Note(4, Fraction.valueOf("1/6")) };
        // Note start = new Note(0, Fraction.valueOf("12"));
        // Fraction bottom = Fraction.valueOf("1/18");
        // Note[] pattern = { new Note(-2, Fraction.valueOf("1/5")), new Note(0,
        // Fraction.valueOf("1/5")), new Note(0, Fraction.valueOf("1/5")), new
        // Note(-2, Fraction.valueOf("1/5")), new Note(-3,
        // Fraction.valueOf("1/5")) };
        // Note start = new Note(0, Fraction.valueOf("10"));
        // Fraction bottom = Fraction.valueOf("2/25");
        Sequence sequence = fractalToSequence(pattern, start, bottom);
        playSequence(sequence);
    }

    private static Sequence fractalToSequence(Note[] pattern, Note start, Fraction bottom) throws Exception
    {
        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, bottom);

        Sequence sequence = new Sequence(Sequence.PPQ, 500);
        Track track = sequence.createTrack();

        long patternTime = addNotesToTrack(track, Arrays.<Note> asList(pattern), 0L, false);
        addNotesToTrack(track, result, patternTime + 1000L, true);

        return sequence;
    }

    private static void playSequence(Sequence sequence) throws MidiUnavailableException, InvalidMidiDataException
    {
        final Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.setSequence(sequence);
        sequencer.open();
        sequencer.start();
        sequencer.addMetaEventListener(new MetaEventListener()
            {
                public void meta(MetaMessage meta)
                {
                    if (meta.getType() == 47)
                    {
                        sequencer.close();
                        System.exit(0);
                    }
                }
            });
    }

    private static long addNotesToTrack(Track track, List<Note> notes, long startTime, boolean extendLastNote)
            throws InvalidMidiDataException
    {
        long time = startTime;
        for (int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);
            int noteNumber = note.getNumber();
            long noteTime = note.getTime();
            if (extendLastNote && i == notes.size() - 1)
            {
                noteTime += 1000L;
            }
            track.add(noteOn(noteNumber, time));
            track.add(noteOff(noteNumber, time + noteTime));
            time += noteTime;
        }
        return time;
    }

    private static MidiEvent noteOn(int noteNumber, long timestamp) throws InvalidMidiDataException
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, 0, noteNumber, 100);
        return new MidiEvent(message, timestamp);
    }

    private static MidiEvent noteOff(int noteNumber, long timestamp) throws InvalidMidiDataException
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_OFF, 0, noteNumber, 0);
        return new MidiEvent(message, timestamp);
    }
}
