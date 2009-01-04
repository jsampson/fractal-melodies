package com.krasama.music.fractal;

import java.util.List;

public class Note
{
    public static final Fraction WHOLE_NOTE = Fraction.valueOf(2000);

    final int pitch;

    final Fraction length;

    final boolean reverse;

    public Note(int pitch, Fraction length)
    {
        this(pitch, length, false);
    }

    public Note(int pitch, Fraction length, boolean reverse)
    {
        this.pitch = pitch;
        this.length = length;
        this.reverse = reverse;
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
        Note[] reversed = new Note[pattern.length];
        for (int i = 0; i < pattern.length; i++)
        {
            reversed[pattern.length - i - 1] = pattern[i];
        }
        iterate(result, pattern, reversed, bottom);
    }

    private void iterate(List<Note> result, Note[] pattern, Note[] reversed, Fraction bottom)
    {
        for (Note note : pattern)
        {
            if (note.length.mul(this.length).compareTo(bottom) < 0)
            {
                result.add(this);
                return;
            }
        }
        for (Note note : (reverse ? reversed : pattern))
        {
            Note scaled = new Note(this.pitch + note.pitch, this.length.mul(note.length), this.reverse ^ note.reverse);
            scaled.iterate(result, pattern, reversed, bottom);
        }
    }
}
