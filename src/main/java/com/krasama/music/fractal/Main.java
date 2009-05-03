package com.krasama.music.fractal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
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
        else if (args.length == 3 && (args[1].equals("play") || args[1].equals("midi")) || args.length == 4 &&
                args[1].equals("save"))
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
        Sequencer sequencer = getSequencer(useSoftwareSynth);
        sequencer.setSequence(sequence);
        sequencer.open();
        sequencer.start();
        displayProgress(sequencer);
        sequencer.close();
        System.exit(0);
    }

    private static void displayProgress(Sequencer sequencer) throws Exception
    {
        long length = sequencer.getMicrosecondLength();
        printProgressBar('_', length, length);
        System.out.println();
        while (sequencer.isRunning())
        {
            long position = sequencer.getMicrosecondPosition();
            printProgressBar('#', length, position);
            Thread.sleep(200);
        }
        printProgressBar('#', length, length);
        System.out.println();
    }

    private static void printProgressBar(char c, long length, long position)
    {
        System.out.print('\r');
        long count = 72 * position / length + 1;
        for (long i = 0; i < count; i++)
        {
            System.out.print(c);
        }
        System.out.print(" " + displayTime(position));
    }

    private static String displayTime(long microseconds)
    {
        long minutes = (microseconds / 1000000) / 60;
        long seconds = (microseconds / 1000000) % 60;
        return String.format("%d:%02d", minutes, seconds);
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
