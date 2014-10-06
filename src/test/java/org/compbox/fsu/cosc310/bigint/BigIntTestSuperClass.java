/*
 * Copyright 2014 Kevin Raoofi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.compbox.fsu.cosc310.bigint;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This class, by itself, is not a valid test!
 *
 * It must be subclassed!
 *
 * @author Kevin Raoofi
 */
public abstract class BigIntTestSuperClass {

    private final Class<? extends BigInt> c;
    private final Constructor<? extends BigInt> strCon;
    private final Constructor<? extends BigInt> intCon;

    public BigIntTestSuperClass(Class<? extends BigInt> c) throws
            NoSuchMethodException {
        this.c = c;
        strCon = c.getConstructor(new Class[]{String.class});
        intCon = c.getConstructor(new Class[]{Integer.TYPE});
    }

    /**
     * Tests a situation in which two numbers are added both of which are
     * negative, has a carry, and requires an extra digit due to the carry.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddBothNegativeCarryDigitAdd() throws Exception {
        BigInt bi = strCon.newInstance("-9");
        bi = bi.add(intCon.newInstance(-4));
        assertEquals("-13", bi.toString());
    }

    /**
     * Tests a situation in which two numbers are added both of which are
     * positive, has a carry, and requires an extra digit due to the carry.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddBothPositiveCarryDigitAdd() throws Exception {
        BigInt bi = strCon.newInstance("9");
        bi = bi.add(intCon.newInstance(4));
        assertEquals("13", bi.toString());
    }

    /**
     * Tests a situation in which two numbers are added both of which are
     * positive, has a carry, and does not require an extra digit due to the
     * carry.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddBothPositiveCarry() throws Exception {
        BigInt bi = strCon.newInstance("15");
        bi = bi.add(new BigIntList(5));
        assertEquals("20", bi.toString());
    }

    /**
     * Tests a situation in which two numbers are added both of which are
     * negative, has a carry, and does not require an extra digit due to the
     * carry.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddBothNegativeCarry() throws Exception {
        BigInt bi = strCon.newInstance("-15");
        bi = bi.add(intCon.newInstance(-5));
        assertEquals("-20", bi.toString());
    }

    @Test
    public void testAddBigNum() throws Exception {
        BigInt bi;
        bi = strCon.newInstance("6000000000000000000000000000001");
        bi = bi.add(new BigIntList(1300));
        assertEquals("6000000000000000000000000001301", bi.toString());
        bi = strCon.newInstance("-6000000000000000000000000000001");
        bi = bi.add(new BigIntList(-1300));
        assertEquals("-6000000000000000000000000001301", bi.toString());
        bi = strCon.newInstance("6000000000000000000000000000001");
        bi = bi.add(strCon.newInstance("6000000000000000000000000000001"));
        assertEquals("12000000000000000000000000000002", bi.toString());
        bi = strCon.newInstance("-6000000000000000000000000000001");
        bi = bi.add(strCon.newInstance("-6000000000000000000000000000001"));
        assertEquals("-12000000000000000000000000000002", bi.toString());
    }

    /**
     * Tests a situation in which two numbers are added both of which are
     * negative, has a carry, and requires an extra digit due to the carry.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddPositiveNegativeCarryDigitAdd() throws Exception {
        BigInt bi = strCon.newInstance("9");
        bi = bi.add(new BigIntList(-4));
        assertEquals("5", bi.toString());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testAllAddMixedSignTypes() throws Exception {
        BigInt bi;
        // 9 - 4 = 5
        bi = new BigIntList(9);
        bi = bi.add(new BigIntList(-4));
        assertEquals(new BigIntList(5), bi);
        // -9 + 4 = -5
        bi = new BigIntList(-9);
        bi = bi.add(new BigIntList(4));
        assertEquals(new BigIntList(-5), bi);
        // -4 + 9 = 5
        bi = new BigIntList(-4);
        bi = bi.add(new BigIntList(9));
        assertEquals(new BigIntList(5), bi);
        // 4 - 9 = -5
        bi = new BigIntList(4);
        bi = bi.add(new BigIntList(-9));
        assertEquals(new BigIntList(-5), bi);
    }

    @Test
    public void testNegate() throws Exception {
        BigInt bi1;
        BigInt bi2;
        bi1 = new BigIntList(1000);
        bi2 = new BigIntList(-1000);
        assertEquals(bi2, bi1.negate());
        assertEquals(bi1, bi2.negate());
        bi1 = new BigIntList(
                "65746546546546546548798765436544321321326874654654");
        bi2 = new BigIntList(
                "-65746546546546546548798765436544321321326874654654");
        assertEquals(bi2, bi1.negate());
        assertEquals(bi1, bi2.negate());
    }

    @Test
    public void testAbs() throws Exception {
        BigInt bi1;
        BigInt bi2;
        bi1 = new BigIntList(1000);
        bi2 = new BigIntList(-1000);
        assertEquals(bi1, bi1.abs());
        assertEquals(bi1, bi2.abs());
        bi1 = strCon.newInstance(
                "65746546546546546548798765436544321321326874654654");
        bi2 = strCon.newInstance(
                "-65746546546546546548798765436544321321326874654654");
        assertEquals(bi1, bi1.abs());
        assertEquals(bi1, bi2.abs());
    }

    @Test
    public void testPositive() throws Exception {
        BigInt bi = new BigIntList(1000);
        assertTrue(bi.isPositive());
        bi = new BigIntList(-1000);
        assertFalse(bi.isPositive());
    }

    /**
     * Test of subtract method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testSubtract() throws Exception {
        BigInt bi;
        // 9 + 4 = 13
        bi = new BigIntList(9);
        bi = bi.subtract(new BigIntList(-4));
        assertEquals(new BigIntList(13), bi);
        // -9 - 4 = -13
        bi = new BigIntList(-9);
        bi = bi.subtract(new BigIntList(4));
        assertEquals(new BigIntList(-13), bi);
        // -4 - 9 = -13
        bi = new BigIntList(-4);
        bi = bi.subtract(new BigIntList(9));
        assertEquals(new BigIntList(-13), bi);
        // 4 + 9 = 13
        bi = new BigIntList(4);
        bi = bi.subtract(new BigIntList(-9));
        assertEquals(new BigIntList(13), bi);
    }

    /**
     * Test of multiply method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testMultiply() throws Exception {
        System.out.println("multiply");
        BigInt instance;
        BigInt expResult;
        BigInt result;
        int o;
        o = 1000;
        instance = intCon.newInstance(99);
        expResult = strCon.newInstance("99000");
        result = instance.multiply(o);
        assertEquals(expResult.toString(), result.toString());
        assertEquals(expResult, result);
        o = -1000;
        instance = intCon.newInstance(99);
        expResult = strCon.newInstance("-99000");
        result = instance.multiply(o);
        assertEquals(expResult.toString(), result.toString());
        assertEquals(expResult, result);
        o = -1000;
        instance = intCon.newInstance(-99);
        expResult = strCon.newInstance("99000");
        result = instance.multiply(o);
        assertEquals(expResult.toString(), result.toString());
        assertEquals(expResult, result);

    }

    @Test
    public void testSteve() throws Exception {

        BigInt instance;
        BigInt expResult;
        BigInt result;
        BigInt o;

        o = intCon.newInstance(25);
        instance = strCon.newInstance("234234235678");
        expResult = strCon.newInstance(Long.toString(234234235678L * 25));

        BigInteger bi = new BigInteger("234234235678");
        bi = bi.multiply(new BigInteger("25"));

        result = instance.multiply(o);
        System.out.println("Long: " + Long.toString(234234235678L * 25));
        System.out.println("Expected " + expResult.toString());
        System.out.println("BigInteger " + bi.toString());
        System.out.println("Actual: " + result.toString());

        assertEquals(bi.toString(), result.toString());
        assertEquals(expResult.toString(), result.toString());
        assertEquals(expResult, result);

        instance = strCon.newInstance("999999999999999");
        o = instance;
        expResult = intCon.newInstance(0);
        result = instance.subtract(o);
        
        assertEquals(expResult.toString(), result.toString());
        assertEquals(expResult, result);

    }

    @Test
    public void incrementable() throws Exception {
        BigInt bi;
        bi = intCon.newInstance(0);
        bi = bi.add(intCon.newInstance(1));
        assertEquals("1", bi.toString());

        bi = intCon.newInstance(0);
        BigInt max = intCon.newInstance(1000);

        for (int i = 0; i < 1000; i++, bi = bi.add(intCon.newInstance(1))) {
            System.out.println(bi.toString());
            assertEquals(Integer.toString(i), bi.toString());
            assertTrue(bi.compareTo(max) < 0);
        }

    }

    @Test
    public void testIncrementCompareTo() throws Exception {
        BigInt bi1 = intCon.newInstance(25);
        BigInt bi2 = intCon.newInstance(16);
        assertTrue("bi1 is not positive", bi1.isPositive());
        assertTrue("bi2 is not positive", bi2.isPositive());
        bi1.compareTo(bi2);
        System.out.println(bi1.compareTo(bi2));
        System.out.println(bi2.compareTo(bi1));
        assertTrue("comparison should return positive; instead returns negative",
                bi1.compareTo(bi2) > 0);

    }

    /**
     * Test of intValue method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testIntValue() throws Exception {
        System.out.println("intValue");
        BigIntList instance = new BigIntList(100);
        int expResult = 100;
        int result = instance.intValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of longValue method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLongValue() throws Exception {
        System.out.println("longValue");
        BigIntList instance = new BigIntList(Long.toString(
                (long) Integer.MAX_VALUE * 2));
        long expResult = (long) Integer.MAX_VALUE * 2;
        long result = instance.longValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of floatValue method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFloatValue() throws Exception {
        System.out.println("floatValue");
        if (!c.isAssignableFrom(Number.class)) {
            System.out.println("Non numeric class; skipping.");
            return;
        }
        Number instance = (Number) strCon.newInstance("1000");

        float expResult = 1000F;
        float result = instance.floatValue();
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of doubleValue method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDoubleValue() throws Exception {
        System.out.println("doubleValue");
        BigIntList instance = new BigIntList(1000);
        double expResult = 1000;
        double result = instance.doubleValue();
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of compareTo method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCompareTo() throws Exception {
        System.out.println("compareTo");
        BigInt instance;
        BigInt o;
        int result;
        instance = new BigIntList(-1000);
        o = new BigIntList(1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = negative; got: " + result,
                result < 0);
        instance = new BigIntList(1000);
        o = new BigIntList(-1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = positive; got: " + result,
                result > 0);
        instance = new BigIntList(1200);
        o = new BigIntList(1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = positive; got: " + result,
                result > 0);
        instance = new BigIntList(1000);
        o = new BigIntList(1200);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = negative; got: " + result,
                result < 0);
        instance = new BigIntList(1000);
        o = new BigIntList(1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = 0; got: " + result, result == 0);
        instance = new BigIntList(-1200);
        o = new BigIntList(-1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = negative; got: " + result,
                result < 0);
        instance = new BigIntList(-1000);
        o = new BigIntList(-1200);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = positive; got: " + result,
                result > 0);
        instance = new BigIntList(-1000);
        o = new BigIntList(-1000);
        result = instance.compareTo(o);
        assertTrue(instance + " - " + o + " = 0; got: " + result, result == 0);
    }

    /**
     * Test of toString method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToString() throws Exception {
        System.out.println("toString");
        BigInt instance = strCon.newInstance("65465465465465");
        String expResult = "65465465465465";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class BigIntList.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testEquals() throws Exception {
        System.out.println("equals");
        Object obj = new BigIntList(0);
        BigInt instance = new BigIntList(0);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

}
