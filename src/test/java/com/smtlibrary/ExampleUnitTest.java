package com.smtlibrary;

import com.smtlibrary.utils.MD5Code;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void md5(){
        MD5Code md5Code = new MD5Code();
        String pass = md5Code.getMD5ofStr("eview2016");
        System.out.println("pass:"+pass);//A85E0847E94E471258F2D6C758E1A9B5
    }
}