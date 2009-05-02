package com.krasama.music.fractal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Song
{
    private static final long TICKS_PER_BEAT = 500L;

    final Scale scale;
    final Note[] pattern;
    final Note start;
    final Fraction bottom;
    final Fraction beats;
    final List<Note> harmonies;

    public Song(Scale scale, Note[] pattern, Note start, Fraction bottom, Fraction beats, List<Note> harmonies)
    {
        this.scale = scale;
        this.pattern = pattern;
        this.start = start;
        this.bottom = bottom;
        this.beats = beats;
        this.harmonies = harmonies;
    }

    public Sequence sequence() throws Exception
    {
        Sequence sequence = new Sequence(Sequence.PPQ, (int) TICKS_PER_BEAT);
        Track track = sequence.createTrack();
        Fraction twoBeats = Fraction.TWO.mul(Fraction.valueOf(TICKS_PER_BEAT));

        int channel = 0;
        Fraction patternTime = addNotesToTrack(track, channel, Arrays.<Note> asList(pattern), Fraction.ZERO, null);

        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, bottom);
        addNotesToTrack(track, channel, result, patternTime.add(twoBeats), twoBeats);

        for (Note harmony : harmonies)
        {
            channel++;
            Note harmonyStart = new Note(harmony.pitch, start.length);
            Fraction harmonyBottom = harmony.length;
            List<Note> harmonyNotes = new ArrayList<Note>();
            harmonyStart.iterate(harmonyNotes, pattern, harmonyBottom);
            addNotesToTrack(track, channel, harmonyNotes, patternTime.add(twoBeats), twoBeats);
        }

        return sequence;
    }

    private Fraction addNotesToTrack(Track track, int channel, List<Note> notes, Fraction startTime, Fraction extendLastNote)
            throws Exception
    {
        Fraction ticksPerMeasure = beats.mul(Fraction.valueOf(TICKS_PER_BEAT));
        Fraction time = startTime;
        for (int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);
            int noteNumber = note.getNoteNumber(scale);
            if (noteNumber < 21 || noteNumber > 108)
            {
                throw new SongException("Notes in this song exceed the physical range of a standard piano.");
            }
            Fraction noteTime = note.length.mul(ticksPerMeasure);
            if (extendLastNote != null && i == notes.size() - 1)
            {
                noteTime = noteTime.add(extendLastNote);
            }
            track.add(noteOn(channel, noteNumber, time.round()));
            time = time.add(noteTime);
            track.add(noteOff(channel, noteNumber, time.round()));
        }
        return time;
    }

    private static MidiEvent noteOn(int channel, int noteNumber, long timestamp) throws Exception
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, channel, noteNumber, 100);
        return new MidiEvent(message, timestamp);
    }

    private static MidiEvent noteOff(int channel, int noteNumber, long timestamp) throws Exception
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_OFF, channel, noteNumber, 0);
        return new MidiEvent(message, timestamp);
    }
}
