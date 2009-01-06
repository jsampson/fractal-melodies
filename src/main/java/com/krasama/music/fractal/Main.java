package com.krasama.music.fractal;

import java.io.File;
import java.io.FileInputStream;
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
        if (args.length == 3 && args[1].equals("play") || args.length == 4 && args[1].equals("save"))
        {
            InputStream file = new FileInputStream(args[0]);
            Map<String, Song> songs;
            try
            {
                songs = Parser.parseSongs(file);
            }
            finally
            {
                file.close();
            }
            Song song = songs.get(args[2].toLowerCase());
            Sequence sequence = song.sequence();
            if (args[1].equals("play"))
            {
                playSequence(sequence);
            }
            else
            {
                saveSequence(sequence, new File(args[3]));
            }
        }
        else
        {
            System.err.println("Usage: Main <song-file> (play <song-name> | save <song-name> <.mid-file>)");
        }
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
