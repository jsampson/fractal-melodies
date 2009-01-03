package com.krasama.music.fractal;

import java.util.List;

public class Note
{
    public static final Fraction WHOLE_NOTE = Fraction.valueOf(2000);

    final int pitch;

    final Fraction length;

    public Note(int pitch, Fraction length)
    {
        this.pitch = pitch;
        this.length = length;
    }

    public int getNumber()
    {
        return Scale.C_MAJOR.get(pitch);
    }

    public long getTime()
    {
        return length.mul(WHOLE_NOTE).longValue();
    }

    public void iterate(List<Note> result, Note[] pattern, Fraction bottom)
    {
        for (Note note : pattern)
        {
            if (note.length.mul(this.length).compareTo(bottom) < 0)
            {
                result.add(this);
                return;
            }
        }
        for (Note note : pattern)
        {
            Note scaled = new Note(this.pitch + note.pitch, this.length.mul(note.length));
            scaled.iterate(result, pattern, bottom);
        }
    }
}
