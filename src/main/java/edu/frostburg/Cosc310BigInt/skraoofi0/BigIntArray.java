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
package edu.frostburg.Cosc310BigInt.skraoofi0;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an array implementation of {@link BigInt}. It's not as robust as
 * {@link BigIntList}. Especially because it'll take forever to do
 * multiplication. But this was my initial implementation which was copied over.
 * 
 * It's actually not used in the program.
 *
 * As an aside, array manipulations are generally a bit easier to work with
 * albeit it requires more cruft due to the necessity of managing array sizes as
 * well as somewhat cryptic array access logic.
 *
 * @author Kevin Raoofi
 */
final class BigIntArray extends Number implements BigInt {

    private static final Logger LOG = Logger.getLogger(BigIntArray.class
            .getName());

    public static final BigIntArray ZERO = new BigIntArray();
    public static final BigInt ONE = new BigIntArray(1);
    public static final BigInt NEGATIVE_ONE = new BigIntArray(-1);

    private static final int NUM_DIGITS_IN_MAX_INT = 11;
    private static final Charset NUM_CHARSET = Charset.isSupported("US-ASCII") ? Charset
            .forName("US-ASCII") : Charset.defaultCharset();

    static void checkArray(final byte[] barr) throws NumberFormatException {
        int counter = -1;
        if (barr[0] == '-' || barr[0] == '+') {
            ++counter;
        }

        while (++counter < barr.length
                && barr[counter] > -1 && barr[counter] < 10);

        if (counter < barr.length) {
            throw new NumberFormatException(
                    "Unsupported character first found at position: "
                    + counter + " in: " + Arrays.toString(barr));
        }
    }

    final byte[] backing;

    public BigIntArray() {
        backing = new byte[]{0};

    }

    public BigIntArray(final int i) {
        if (i == 0) {
            backing = new byte[]{0};
            return;
        }
        final byte[] tmpBacking = new byte[NUM_DIGITS_IN_MAX_INT];
        int counter = tmpBacking.length - 1;
        int value = i;

        for (; value != 0; counter--, value /= 10) {
            tmpBacking[counter] = (byte) Math.abs(value % 10);
        }

        if (i < 0) {
            tmpBacking[counter--] = '-';
        }

        backing = Arrays.copyOfRange(tmpBacking, counter + 1, tmpBacking.length);
        checkArray(backing);
        LOG.finest(Arrays.toString(this.backing));
    }

    /**
     *
     * @param s
     * @throws NumberFormatException
     */
    public BigIntArray(final String s) throws NumberFormatException {
        final byte[] tmpBacking = s.getBytes(NUM_CHARSET);
        for (int i = 0; i < tmpBacking.length; i++) {
            if (tmpBacking[i] > '-') {
                tmpBacking[i] -= '0';
            }
        }
        LOG.finest(Arrays.toString(tmpBacking));
        checkArray(tmpBacking);
        backing = tmpBacking;
    }

    public BigIntArray(final byte[] barr) throws NumberFormatException {
        checkArray(barr);
        backing = Arrays.copyOf(barr, barr.length);
    }

    @Override
    public BigIntArray add(final BigInt other) {
        BigIntArray o = new BigIntArray(other.toString());

        /*
         * Prepare your anus; this method is ridiculous.
         *
         * You want recursion? We've got it. You want iterative? We've got it.
         */
        if (this.abs().compareTo(o.abs()) < 0) {
            // if the absolute value of this is less than other, swap operands
            return o.add(this);
        }

        if (this.compareTo(o) < 0) {
            // if this value is less than the other,
            // negate operands and negate result
            return this.negate().add(o.negate()).negate();
        }

        // We need to know the sizes of the bigger array number
        final int bigSize = backing.length > o.backing.length
                ? backing.length : o.backing.length;

        // Whether this object is positive
        final boolean thisPositive = isPositive();
        // Whether the object that is being added is positive
        final boolean otherPositive = o.isPositive();

        // We need to know if the signs if the numbers are the same.
        // If they aren't, we need to subtract instead of add
        final boolean add = thisPositive == otherPositive;


        /*
         * number of values to reserve for carries
         */
        final int reserved = 1;

        /*
         * Temporary array for the resulting backend.
         */
        final byte[] result = new byte[bigSize + reserved];

        /**
         * The carry value -- we can initialize it to 0 for now. Every addition
         * operation which creates a carry will set it to 1. Every subtraction
         * operation which creates a carry will set it to -1. It should never be
         * any other value
         */
        byte carry = 0;

        /**
         * The number of total characters digits in the number takes up
         */
        int digitCount = 0;

        /*
         * the number of characters required if a sign is used
         */
        int signCount;

        /*
         * the number of total characters used. That is, if there is a sign
         * character, numberCount = signCount and otherwise, numberCount =
         * digitCount
         */
        int numberCount;

        for (int i = 0; i <= bigSize; i++) {
            int ri = result.length - 1 - i;

            result[ri] += carry;
            carry = 0;

            // if this backing is big enough to access the index
            if (i < backing.length) {
                byte thisElement = backing[backing.length - 1 - i];
                // if this element is not a positive or negative sign
                if (thisElement < 10 && thisElement > -10) {
                    digitCount = i;
                    if (add || thisPositive) {
                        result[ri] += thisElement;
                    } else {
                        result[ri] -= thisElement;
                    }
                }
            }

            if (i < o.backing.length) {
                byte otherElement = o.backing[o.backing.length - 1 - i];
                // if the other element is not a positive or negative sign
                if (otherElement < 10 && otherElement > -10) {
                    digitCount = i;
                    if (add || otherPositive) {
                        result[ri] += otherElement;
                    } else {
                        result[ri] -= otherElement;
                    }
                }
            }

            if (result[ri] > 9) { // this can only happen when add is true
                result[ri] -= 10;
                carry = 1;
                digitCount = i + 1;
            } else if (result[ri] < 0) { // this can only happen when !add
                result[ri] += 10;
                carry = -1;
                digitCount = i + 1;
            }
        }

        // align from the index to count
        digitCount++;

        // the number of characters used if sign has to put in
        signCount = digitCount + 1;

        // let's assume no sign for now.
        numberCount = digitCount;

        if (add && !thisPositive && !otherPositive) {
            // give an extra space for the sign character
            numberCount = digitCount + 1;
            result[result.length - signCount] = '-';
        }

        return new BigIntArray(Arrays.copyOfRange(result,
                result.length - numberCount,
                result.length));
    }

    @Override
    public BigIntArray subtract(final BigInt o) {
        return add(o.negate());
    }

    @Override
    public BigIntArray multiply(final BigInt bi) {
        BigInt bia = new BigIntArray(bi.toString());
        BigInt operand = bia.abs();
        BigIntArray result = ZERO;
        for (BigInt i = ZERO; i.compareTo(operand) < 0; i = i.add(ONE)) {
            result = result.add(this);
        }

        if (!bia.isPositive()) {
            result = result.negate();
        }

        return result;
    }

    @Override
    public BigIntArray multiply(int o) {
        int operand = Math.abs(o);
        BigIntArray result = ZERO;
        for (int i = 0; i < operand; i++) {
            result = result.add(this);
        }

        if (o < 0) {
            result = result.negate();
        }

        return result;
    }

    @Override
    public BigIntArray negate() {
        if (!isPositive()) {
            return new BigIntArray(Arrays
                    .copyOfRange(backing, 1, backing.length));
        } else {
            byte[] results = new byte[backing.length + 1];
            System.arraycopy(backing, 0, results,
                    1, backing.length);
            results[0] = '-';
            return new BigIntArray(results);
        }
    }

    @Override
    public BigIntArray abs() {
        final boolean thisPositive = isPositive();
        if (!thisPositive) {
            return this.negate();
        }
        return this;
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return Long.valueOf(toString());
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }

    @Override
    public int compareTo(BigInt o) {
        BigIntArray bia = new BigIntArray(o.toString());

        final boolean thisPositive = isPositive();
        final boolean otherPositive = bia.isPositive();
        final boolean bothPositive = thisPositive && otherPositive;

        if (thisPositive && !otherPositive) {
            return 1;
        } else if (!thisPositive && otherPositive) {
            return -1;
        }

        if (thisPositive == otherPositive) {
            int result = backing.length - bia.backing.length;
            result = bothPositive ? result : -result;
            if (result != 0) {
                return result;
            }
        }

        /*
         * We have to iterate the array now, so we need to know the size of the
         * bigger array.
         */
        final int bigSize = backing.length > bia.backing.length
                ? backing.length : bia.backing.length;

        for (int i = 0; i < bigSize; i++) {
            final int delta
                    = backing[i]
                    - bia.backing[i];
            if (delta != 0) {
                return bothPositive ? delta : -delta;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        if (backing.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        int i = 0;
        if (backing[0] == '+' || backing[0] == '-') {
            sb.append((char) backing[i]);
            i++;
        }

        for (; i < backing.length; i++) {
            sb.append(Byte.valueOf(backing[i]));
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(this.backing);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BigIntArray other = (BigIntArray) obj;
        if (!Arrays.equals(this.backing, other.backing)) {
            return false;
        }
        return true;
    }

    public static void main(String... args) {
        Handler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        LOG.addHandler(h);
        LOG.setLevel(Level.ALL);

        BigIntArray bi = new BigIntArray("-9");
        bi = bi.add(new BigIntArray(-4));

        System.out.println(Arrays.toString(bi.backing));
        System.out.println(bi);
    }

    @Override
    public boolean isPositive() {
        return this.backing[0] != '-';
    }

}
