package com.krasama.music.fractal;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Note[] pattern = { new Note(-1, Fraction.valueOf("1/4")), new Note(0,
        // Fraction.valueOf("1/2")), new Note(1, Fraction.valueOf("1/4")) };
        // Note start = new Note(0, Fraction.valueOf("4"));
        // Fraction bottom = Fraction.valueOf("1/8");
        // Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0,
        // Fraction.valueOf("1/4")),
        // new Note(1, Fraction.valueOf("1/2")) };
        // Note start = new Note(0, Fraction.valueOf("4"));
        // Fraction bottom = Fraction.valueOf("1/8");
        Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")),
                new Note(1, Fraction.valueOf("1/2"), true) };
        Note start = new Note(0, Fraction.valueOf("128"));
        Fraction bottom = Fraction.valueOf("1/16");
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
        Song song = new Song(Scale.C_MAJOR, pattern, start, bottom, Fraction.valueOf(4));
        Sequence sequence = song.sequence();
        playSequence(sequence);
    }

    public static void playSequence(Sequence sequence) throws MidiUnavailableException, InvalidMidiDataException
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

    public static void saveSequence(Sequence sequence, File file) throws Exception
    {
        MidiSystem.write(sequence, 0, file);
    }
}
