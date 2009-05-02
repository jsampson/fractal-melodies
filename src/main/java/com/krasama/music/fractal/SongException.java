package com.krasama.music.fractal;

public class SongException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public SongException()
    {
        super();
    }

    public SongException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SongException(String message)
    {
        super(message);
    }

    public SongException(Throwable cause)
    {
        super(cause);
    }
}
