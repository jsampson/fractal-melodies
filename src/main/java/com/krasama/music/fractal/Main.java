package com.krasama.music.fractal;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        InputStream file = Main.class.getResourceAsStream("/example-songs.txt");
        Map<String, Song> songs = Parser.parseSongs(file);
        Song song = songs.get("d+");
        Sequence sequence = song.sequence();
        playSequence(sequence);
    }

    public static void playSequence(Sequence sequence) throws Exception
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
