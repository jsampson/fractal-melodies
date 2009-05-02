package com.krasama.music.fractal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiDevice.Info;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        if (args.length == 1 && args[0].equals("info"))
        {
            Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (Info info : infos)
            {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                Class<?> deviceClass = device.getClass();
                Class<?>[] deviceInterfaces = deviceClass.getInterfaces();
                System.out.println(info.getName() + " (" + info.getVersion() + ", " + info.getVendor() + ")");
                System.out.println("    Description: " + info.getDescription());
                System.out.println("    Class: " + deviceClass.getName());
                for (Class<?> deviceInterface : deviceInterfaces)
                {
                    System.out.println("    Interface: " + deviceInterface.getName());
                }
            }
        }
        else if (args.length == 3 && (args[1].equals("play") || args[1].equals("midi")) || args.length == 4 && args[1].equals("save"))
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
                playSequence(sequence, true);
            }
            else if (args[1].equals("midi"))
            {
                playSequence(sequence, false);
            }
            else
            {
                saveSequence(sequence, new File(args[3]));
            }
        }
        else
        {
            System.err.println("Usage: ./frac info");
            System.err.println("       ./frac <song-file> play <song-name>");
            System.err.println("       ./frac <song-file> midi <song-name>");
            System.err.println("       ./frac <song-file> save <song-name> <.mid-file>");
            System.exit(1);
        }
    }

    public static void playSequence(Sequence sequence, boolean useSoftwareSynth) throws Exception
    {
        final Sequencer sequencer = getSequencer(useSoftwareSynth);
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

    private static Sequencer getSequencer(boolean useSoftwareSynth) throws Exception
    {
        if (useSoftwareSynth)
        {
            return MidiSystem.getSequencer(true);
        }
        else
        {
            Sequencer sequencer = MidiSystem.getSequencer(false);
            sequencer.getTransmitter().setReceiver(MidiSystem.getReceiver());
            return sequencer;
        }
    }

    public static void saveSequence(Sequence sequence, File file) throws Exception
    {
        MidiSystem.write(sequence, 0, file);
    }
}
