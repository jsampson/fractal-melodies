package com.krasama.music.fractal;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
//        Note[] pattern = { new Note(-1, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/2")), new Note(1, Fraction.valueOf("1/4")) };
//        Note start = new Note(0, Fraction.valueOf("4"));
//        Fraction bottom = Fraction.valueOf("1/8");
//        Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/2")) };
//        Note start = new Note(0, Fraction.valueOf("4"));
//        Fraction bottom = Fraction.valueOf("1/8");
        Note[] pattern = { new Note(-2, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/2"), true) };
        Note start = new Note(0, Fraction.valueOf("8"));
        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(-2, Fraction.valueOf("1/2")), new Note(3, Fraction.valueOf("1/2")) };
//        Note start = new Note(0, Fraction.valueOf("16"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(-1, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")) };
//        Note start = new Note(0, Fraction.valueOf("64"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(0, Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/2")) };
//        Note start = new Note(0, Fraction.valueOf("32"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(0, Fraction.valueOf("1/4")), new Note(1, Fraction.valueOf("1/4")), new Note(-1, Fraction.valueOf("1/4")), new Note(0, Fraction.valueOf("1/4")) };
//        Note start = new Note(0, Fraction.valueOf("64"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(0, Fraction.valueOf("1/8")), new Note(-1, Fraction.valueOf("1/8")), new Note(0, Fraction.valueOf("3/4")) };
//        Note start = new Note(0, Fraction.valueOf("32"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note[] pattern = { new Note(0, Fraction.valueOf("1/8")), new Note(-3, Fraction.valueOf("1/8")), new Note(0, Fraction.valueOf("3/4")) };
//        Note start = new Note(0, Fraction.valueOf("32"));
//        Fraction bottom = Fraction.valueOf("1/16");
//        Note start = new Note(0, Fraction.valueOf("1"));
//        Fraction bottom = Fraction.valueOf("1/4");
//        Note[] pattern = { new Note(2, Fraction.valueOf("1/6")), new Note(1, Fraction.valueOf("1/6")), new Note(0, Fraction.valueOf("1/6")), new Note(3, Fraction.valueOf("1/6")), new Note(3, Fraction.valueOf("1/6")), new Note(4, Fraction.valueOf("1/6")) };
//        Note start = new Note(0, Fraction.valueOf("12"));
//        Fraction bottom = Fraction.valueOf("1/18");
//        Note[] pattern = { new Note(-2, Fraction.valueOf("1/5")), new Note(0, Fraction.valueOf("1/5")), new Note(0, Fraction.valueOf("1/5")), new Note(-2, Fraction.valueOf("1/5")), new Note(-3, Fraction.valueOf("1/5")) };
//        Note start = new Note(0, Fraction.valueOf("10"));
//        Fraction bottom = Fraction.valueOf("2/25");
        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, bottom);
        play(result);
        System.exit(0);
    }

    private static void play(List<Note> notes) throws Exception
    {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        try
        {
            MidiChannel chan = synth.getChannels()[0];
            Thread.sleep(1000L);
            long start = System.currentTimeMillis();
            long target = start;
            for (int i = 0; i < notes.size(); i++)
            {
                Note note = notes.get(i);
                int noteNumber = note.getNumber();
                long noteTime = note.getTime();
                if (i == notes.size() - 1)
                {
                    noteTime += 1000L;
                }
                target += noteTime;
                long sleepTime = target - System.currentTimeMillis();
                if (sleepTime > 0)
                {
                    chan.noteOn(noteNumber, 100);
                    Thread.sleep(sleepTime);
                    chan.noteOff(noteNumber);
                }
            }
        }
        finally
        {
            synth.close();
        }
    }
}
