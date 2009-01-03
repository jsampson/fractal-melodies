package com.krasama.music.fractal;

import junit.framework.TestCase;

public class FractionTest extends TestCase
{
    public void testZero()
    {
        Fraction zero = Fraction.ZERO;
        assertEquals(zero, zero);
    }

    public void testOne()
    {
        Fraction zero = Fraction.ZERO;
        Fraction one = Fraction.ONE;
        assertEquals(one, one);
        assertEquals(-1, zero.compareTo(one));
        assertEquals(0, zero.compareTo(zero));
        assertEquals(0, one.compareTo(one));
        assertEquals(1, one.compareTo(zero));
    }

    public void testOneHalf()
    {
        Fraction zero = Fraction.ZERO;
        Fraction one = Fraction.ONE;
        Fraction two = Fraction.TWO;
        Fraction half = one.div(two);
        assertEquals(half, half);
        assertEquals(-1, zero.compareTo(half));
        assertEquals(0, half.compareTo(half));
        assertEquals(1, half.compareTo(zero));
        assertEquals(-1, half.compareTo(one));
        assertEquals(1, one.compareTo(half));
    }

    public void testValueOfInt()
    {
        assertEquals(Fraction.valueOf(5), Fraction.valueOf(5));
    }

    public void testToString()
    {
        assertEquals("0", Fraction.ZERO.toString());
        assertEquals("1", Fraction.ONE.toString());
        assertEquals("2", Fraction.TWO.toString());
        assertEquals("1/2", Fraction.ONE.div(Fraction.TWO).toString());
    }

    public void testValueOfString()
    {
        assertEquals("0", Fraction.valueOf("0").toString());
        assertEquals("5", Fraction.valueOf("5").toString());
        assertEquals("2/3", Fraction.valueOf("4/6").toString());
    }

    public void testNormalization()
    {
        assertEquals("3/4", Fraction.valueOf(6).div(Fraction.valueOf(8)).toString());
    }

    public void testMultiply()
    {
        Fraction twoThirds = Fraction.valueOf(2).div(Fraction.valueOf(3));
        Fraction eightFifths = Fraction.valueOf(8).div(Fraction.valueOf(5));
        assertEquals("16/15", twoThirds.mul(eightFifths).toString());
    }

    public void testAdd()
    {
        Fraction twoThirds = Fraction.valueOf(2).div(Fraction.valueOf(3));
        Fraction eightFifths = Fraction.valueOf(8).div(Fraction.valueOf(5));
        assertEquals("34/15", twoThirds.add(eightFifths).toString());
    }

    public void testSubtract()
    {
        Fraction twoThirds = Fraction.valueOf(2).div(Fraction.valueOf(3));
        Fraction eightFifths = Fraction.valueOf(8).div(Fraction.valueOf(5));
        assertEquals("14/15", eightFifths.sub(twoThirds).toString());
    }

    public void testSubtractTooMuch()
    {
        Fraction twoThirds = Fraction.valueOf(2).div(Fraction.valueOf(3));
        Fraction eightFifths = Fraction.valueOf(8).div(Fraction.valueOf(5));
        try
        {
            twoThirds.sub(eightFifths);
            fail();
        }
        catch (IllegalArgumentException okay)
        {
        }
    }

    public void testNegative()
    {
        try
        {
            Fraction.valueOf(-1);
            fail();
        }
        catch (IllegalArgumentException okay)
        {
        }
    }
}
