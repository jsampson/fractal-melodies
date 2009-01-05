package com.krasama.music.fractal;

import java.math.BigInteger;

public class Fraction
{
    public static final Fraction ZERO = Fraction.valueOf(0);

    public static final Fraction ONE = Fraction.valueOf(1);

    public static final Fraction TWO = Fraction.valueOf(2);

    public static final Fraction HALF = ONE.div(TWO);

    public static Fraction valueOf(long i)
    {
        return new Fraction(BigInteger.valueOf(i), BigInteger.ONE);
    }

    public static Fraction valueOf(String s)
    {
        int slash = s.indexOf('/');
        if (slash != -1)
        {
            String num = s.substring(0, slash);
            String den = s.substring(slash + 1);
            return new Fraction(new BigInteger(num), new BigInteger(den));
        }
        else
        {
            return new Fraction(new BigInteger(s), BigInteger.ONE);
        }
    }

    private final BigInteger num;

    private final BigInteger den;

    private Fraction(BigInteger num, BigInteger den)
    {
        if (num.signum() < 0 || den.signum() <= 0)
        {
            throw new IllegalArgumentException();
        }
        BigInteger gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof Fraction ? equals((Fraction) other) : false;
    }

    public boolean equals(Fraction other)
    {
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        if (den.equals(BigInteger.ONE))
        {
            return num.toString();
        }
        else
        {
            return num + "/" + den;
        }
    }

    public int compareTo(Fraction other)
    {
        return this.num.multiply(other.den).compareTo(other.num.multiply(this.den));
    }

    public Fraction div(Fraction other)
    {
        return new Fraction(this.num.multiply(other.den), other.num.multiply(this.den));
    }

    public Fraction mul(Fraction other)
    {
        return new Fraction(this.num.multiply(other.num), this.den.multiply(other.den));
    }

    public Fraction add(Fraction other)
    {
        return new Fraction(this.num.multiply(other.den).add(other.num.multiply(this.den)), this.den.multiply(other.den));
    }

    public Fraction sub(Fraction other)
    {
        return new Fraction(this.num.multiply(other.den).subtract(other.num.multiply(this.den)), this.den.multiply(other.den));
    }

    public long round()
    {
        return this.add(HALF).floor();
    }

    public long floor()
    {
        return num.divide(den).longValue();
    }
}
