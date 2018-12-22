package com.example.gal.shotafim.WorkingClasses;
import java.security.SecureRandom;
import java.math.BigInteger;

public final class Generator
{

    private static SecureRandom random = new SecureRandom();

    public static String nextSessionId()
    {
        return new BigInteger(32, random).toString(32);
    }

}