import java.util.List;

public class Note
{
    public static final int[] MAJOR_SCALE = { 0, 2, 4, 5, 7, 9, 11 };

    public static final int MIDDLE_C = 60;

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
        int p = pitch;
        int n = MIDDLE_C;
        while (p < 0)
        {
            p += 7;
            n -= 12;
        }
        while (p > 6)
        {
            p -= 7;
            n += 12;
        }
        return n + MAJOR_SCALE[p];
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
