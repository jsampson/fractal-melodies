package com.krasama.music.fractal;

import junit.framework.TestCase;

public class ScaleTest extends TestCase
{
    public void testMiddle()
    {
        Scale scale = new Scale(50, 1);
        assertEquals(50, scale.get(0));
    }

    public void testSimpleStep()
    {
        Scale scale = new Scale(50, 1);
        assertEquals(48, scale.get(-2));
        assertEquals(49, scale.get(-1));
        assertEquals(50, scale.get(0));
        assertEquals(51, scale.get(1));
        assertEquals(52, scale.get(2));
    }

    public void testLargerStep()
    {
        Scale scale = new Scale(50, 2);
        assertEquals(46, scale.get(-2));
        assertEquals(48, scale.get(-1));
        assertEquals(50, scale.get(0));
        assertEquals(52, scale.get(1));
        assertEquals(54, scale.get(2));
    }

    public void testOddSteps()
    {
        Scale scale = new Scale(50, 2, 5);
        assertEquals(40, scale.get(-4));
        assertEquals(42, scale.get(-3));
        assertEquals(45, scale.get(-2));
        assertEquals(47, scale.get(-1));
        assertEquals(50, scale.get(0));
        assertEquals(52, scale.get(1));
        assertEquals(55, scale.get(2));
        assertEquals(57, scale.get(3));
        assertEquals(60, scale.get(4));
    }

    public void testMajorScale()
    {
        assertEquals(48, Scale.C_MAJOR.get(-7));
        assertEquals(50, Scale.C_MAJOR.get(-6));
        assertEquals(52, Scale.C_MAJOR.get(-5));
        assertEquals(53, Scale.C_MAJOR.get(-4));
        assertEquals(55, Scale.C_MAJOR.get(-3));
        assertEquals(57, Scale.C_MAJOR.get(-2));
        assertEquals(59, Scale.C_MAJOR.get(-1));
        assertEquals(60, Scale.C_MAJOR.get(0));
        assertEquals(62, Scale.C_MAJOR.get(1));
        assertEquals(64, Scale.C_MAJOR.get(2));
        assertEquals(65, Scale.C_MAJOR.get(3));
        assertEquals(67, Scale.C_MAJOR.get(4));
        assertEquals(69, Scale.C_MAJOR.get(5));
        assertEquals(71, Scale.C_MAJOR.get(6));
        assertEquals(72, Scale.C_MAJOR.get(7));
    }

    public void testValidation()
    {
        checkBadScale(-1, 1, 2);
        checkBadScale(128, 1, 2);
        checkBadScale(60);
        checkBadScale(60, -1);
        checkBadScale(60, 0);
        checkBadScale(60, 2, 2);
    }

    private void checkBadScale(int middle, int... steps)
    {
        try
        {
            new Scale(middle, steps);
            fail();
        }
        catch (IllegalArgumentException expected)
        {
        }
    }
}
