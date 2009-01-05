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

    public Song(Scale scale, Note[] pattern, Note start, Fraction bottom, Fraction beats)
    {
        this.scale = scale;
        this.pattern = pattern;
        this.start = start;
        this.bottom = bottom;
        this.beats = beats;
    }

    public Sequence sequence() throws Exception
    {
        List<Note> result = new ArrayList<Note>();
        start.iterate(result, pattern, bottom);

        Sequence sequence = new Sequence(Sequence.PPQ, (int) TICKS_PER_BEAT);
        Track track = sequence.createTrack();

        long patternTime = addNotesToTrack(track, Arrays.<Note> asList(pattern), 0L, false);
        addNotesToTrack(track, result, patternTime + 2 * TICKS_PER_BEAT, true);

        return sequence;
    }

    private long addNotesToTrack(Track track, List<Note> notes, long startTime, boolean extendLastNote) throws Exception
    {
        Fraction ticksPerMeasure = beats.mul(Fraction.valueOf(TICKS_PER_BEAT));
        long time = startTime;
        for (int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);
            int noteNumber = note.getNoteNumber(scale);
            long noteTime = note.getTicks(ticksPerMeasure);
            if (extendLastNote && i == notes.size() - 1)
            {
                noteTime += 2 * TICKS_PER_BEAT;
            }
            track.add(noteOn(noteNumber, time));
            track.add(noteOff(noteNumber, time + noteTime));
            time += noteTime;
        }
        return time;
    }

    private static MidiEvent noteOn(int noteNumber, long timestamp) throws Exception
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, 0, noteNumber, 100);
        return new MidiEvent(message, timestamp);
    }

    private static MidiEvent noteOff(int noteNumber, long timestamp) throws Exception
    {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_OFF, 0, noteNumber, 0);
        return new MidiEvent(message, timestamp);
    }
}
