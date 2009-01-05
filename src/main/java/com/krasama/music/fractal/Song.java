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

        Fraction twoBeats = Fraction.TWO.mul(Fraction.valueOf(TICKS_PER_BEAT));
        Fraction patternTime = addNotesToTrack(track, Arrays.<Note> asList(pattern), Fraction.ZERO, null);
        addNotesToTrack(track, result, patternTime.add(twoBeats), twoBeats);

        return sequence;
    }

    private Fraction addNotesToTrack(Track track, List<Note> notes, Fraction startTime, Fraction extendLastNote) throws Exception
    {
        Fraction ticksPerMeasure = beats.mul(Fraction.valueOf(TICKS_PER_BEAT));
        Fraction time = startTime;
        for (int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);
            int noteNumber = note.getNoteNumber(scale);
            Fraction noteTime = note.length.mul(ticksPerMeasure);
            if (extendLastNote != null && i == notes.size() - 1)
            {
                noteTime = noteTime.add(extendLastNote);
            }
            track.add(noteOn(noteNumber, time.round()));
            time = time.add(noteTime);
            track.add(noteOff(noteNumber, time.round()));
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
