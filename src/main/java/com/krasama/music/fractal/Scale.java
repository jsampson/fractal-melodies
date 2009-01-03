package com.krasama.music.fractal;

public class Scale
{
    public static final Scale C_MAJOR = new Scale(60, 2, 4, 5, 7, 9, 11, 12);

    private final int middle;
    private final int[] steps;

    public Scale(int middle, int... steps)
    {
        if (middle < 0 || middle > 127)
        {
            throw new IllegalArgumentException("middle must be a valid MIDI pitch (0..127)");
        }
        if (steps.length == 0)
        {
            throw new IllegalArgumentException("steps must not be empty");
        }
        if (steps[0] <= 0)
        {
            throw new IllegalArgumentException("steps must be positive");
        }
        for (int i = 1; i < steps.length; i++)
        {
            if (steps[i] <= steps[i - 1])
            {
                throw new IllegalArgumentException("steps must be increasing");
            }
        }
        this.middle = middle;
        this.steps = steps;
    }

    public int get(int offset)
    {
        int result = middle;
        while (offset < 0)
        {
            offset += steps.length;
            result -= steps[steps.length - 1];
        }
        while (offset >= steps.length)
        {
            offset -= steps.length;
            result += steps[steps.length - 1];
        }
        if (offset > 0)
        {
            result += steps[offset - 1];
        }
        return result;
    }
}
