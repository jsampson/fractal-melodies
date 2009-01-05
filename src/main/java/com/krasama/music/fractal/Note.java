package com.krasama.music.fractal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Note
{
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

    public int getNoteNumber(Scale scale)
    {
        return scale.get(pitch);
    }

    public long getTicks(Fraction ticksPerMeasure)
    {
        return length.mul(ticksPerMeasure).round();
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

    public boolean equals(Object other)
    {
        return other instanceof Note && equals((Note) other);
    }

    public boolean equals(Note other)
    {
        return this.pitch == other.pitch && this.length.equals(other.length) && this.reverse == other.reverse;
    }

    public int hashCode()
    {
        throw new UnsupportedOperationException();
    }

    public String toString()
    {
        return pitch + " " + (reverse ? "-" : "") + length;
    }

    public static Note valueOf(String string)
    {
        Pattern pattern = Pattern.compile("([-+]?[0-9]+) (-?)([0-9]+(/[0-9]+)?)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches())
        {
            int pitch = Integer.parseInt(matcher.group(1));
            Fraction length = Fraction.valueOf(matcher.group(3));
            boolean reverse = matcher.group(2).equals("-");
            return new Note(pitch, length, reverse);
        }
        else
        {
            throw new IllegalArgumentException("invalid note: " + string);
        }
    }
}
