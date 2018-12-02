package com.uottawa.olympus.olympusservices;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordTest {

    @Test
    public void testHashes(){
        String string1 = PasswordEncryption.encrypt("helloworld", "i]/S9evY\\,");
        String string2 = PasswordEncryption.encrypt("helloworld", "i]/S9evY\\,");

        assertTrue(PasswordEncryption.slowEquals(string1, string2));

        String string3 = PasswordEncryption.encrypt("helloworld", "i]/S9evY\\");
        assertTrue(!PasswordEncryption.slowEquals(string1, string3));

        String string4 = PasswordEncryption.encrypt("helloworl", "i]/S9evY\\,");
        assertTrue(!PasswordEncryption.slowEquals(string1, string4));

    }
}
