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
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * This is the BigInt implementation. It uses a regular Java List to do all data
 * storage and can be made to work with standard Java collections. This allows
 * the use of signed values and can add, subtract, or multiply. However, it
 * cannot do division.
 *
 * @author Kevin Raoofi
 */
public class BigIntList extends Number implements BigInt {

    private static final Logger LOG = Logger.getLogger(BigIntList.class
            .getName());

    /**
     * Responsible for creating List implementation instances. You can switch
     * this to using standard java collections
     */
    private static final ListFactory factory = new ListFactory(
            edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList.class);

    /**
     * Constant value for 0
     */
    public static final BigIntList ZERO = new BigIntList();
    /**
     * Constant value for 1
     */
    public static final BigInt ONE = new BigIntList(1);
    /**
     * Constant value for -1
     */
    public static final BigInt NEGATIVE_ONE = new BigIntList(-1);

    /**
     * This is the charset used to decode a String to bytes. I need to assume
     * ASCII as I'm doing arithmetic to convert from char to byte
     */
    private static final Charset NUM_CHARSET = Charset.isSupported("US-ASCII") ? Charset
            .forName("US-ASCII") : Charset.defaultCharset();

    /**
     * Iterates through list and verifies that each element is within bounds.
     *
     * @param bl a list containing Byte objects
     * @throws NumberFormatException if any of the bytes are 10 or above or less
     * than 0 except for the first one which may be '-' or '+'
     */
    static void checkList(final List<Byte> bl) throws NumberFormatException {
        int counter = -1;
        if (bl.get(0) == '-' || bl.get(0) == '+') {
            ++counter;
        }

        while (++counter < bl.size()
                && bl.get(counter) > -1 && bl.get(counter) < 10);

        if (counter < bl.size()) {
            throw new NumberFormatException(
                    "Unsupported character first found at position: "
                    + counter + " in: " + bl.toString());
        }
    }

    /**
     * The backing collection of the object
     */
    private final List<Byte> backing;

    /**
     * Constructor that initializes to 0. The preferred way to get 0 is to use
     * the constant {@link #ZERO}.
     */
    public BigIntList() {
        backing = factory.create();
        backing.add((byte) 0);
    }

    /**
     * Creates an object using the given value
     *
     * @param i the integer to convert
     */
    public BigIntList(final int i) {
        if (i == 0) {
            backing = factory.create();
            backing.add((byte) 0);
            return;
        }
        final List<Byte> tmpBacking = factory.create();
        int counter = tmpBacking.size() - 1;
        int value = i;

        for (; value != 0; counter--, value /= 10) {
            tmpBacking.add(0, (byte) Math.abs(value % 10));
        }

        if (i < 0) {
            tmpBacking.add(0, (byte) '-');
        }

        backing = tmpBacking;
        checkList(backing);
    }

    /**
     * Creates an object using a String.
     *
     * @param s string representation of object
     * @throws NumberFormatException if the String contains values which are
     * invalid
     */
    public BigIntList(final String s) throws NumberFormatException {
        final byte[] tmpBackingArr = s.getBytes(NUM_CHARSET);
        final List<Byte> tmpBacking = factory.create();

        for (Byte b : tmpBackingArr) {
            tmpBacking.add(b);
        }

        ListIterator<Byte> li = tmpBacking.listIterator();

        while (li.hasNext()) {
            byte b = li.next();
            if (b > '-') {
                li.set((byte) (b - '0'));
            }
        }

        checkList(tmpBacking);
        backing = tmpBacking;
    }

    /**
     * Allows the use of a collection of bytes to initialize the object. You
     * should note, however, that it is expected that each byte is within the
     * bounds of 0 to 9, '-', or '+'. Rather than the case with
     * {@link #BigIntList(java.lang.String)} where each byte represents the
     * ASCII version of each digit.
     *
     * @param barr a collection of bytes
     * @throws NumberFormatException throws an exception if values are invalid
     */
    public BigIntList(final Collection<Byte> barr) throws NumberFormatException {
        if (barr.isEmpty()) {
            backing = ZERO.backing;
        } else {
            backing = factory.create(barr);
        }
        checkList(backing);
    }

    @Override
    public BigIntList add(final BigInt other) {
        BigIntList o = new BigIntList(other.toString());

        /*
         * Prepare yours; this method is ridiculous.
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
        final int bigSize = backing.size() > o.backing.size()
                ? backing.size() : o.backing.size();

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
        final List<Byte> result = factory.create();

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
        int digitCount;

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
            //int ri = result.size() - 1 - i;

            result.add(0, (byte) (0 + carry));
            carry = 0;

            // if this backing is big enough to access the index
            if (i < backing.size()) {
                int thisElement = backing.get(backing.size() - 1 - i);
                // if this element is not a positive or negative sign
                if (thisElement < 10 && thisElement > -10) {
                    if (add || thisPositive) {
                        result.set(0, (byte) (result.get(0) + thisElement));
                    } else {
                        result.set(0, (byte) (result.get(0) - thisElement));
                    }
                }
            }

            if (i < o.backing.size()) {
                int otherElement = o.backing.get(o.backing.size() - 1 - i);
                // if the other element is not a positive or negative sign
                if (otherElement < 10 && otherElement > -10) {
                    if (add || otherPositive) {
                        result.set(0, (byte) (result.get(0) + otherElement));
                    } else {
                        result.set(0, (byte) (result.get(0) - otherElement));
                    }
                }
            }

            if (result.get(0) > 9) { // this can only happen when add is true
                result.set(0, (byte) (result.get(0) - 10));
                carry = 1;
            } else if (result.get(0) < 0) { // this can only happen when !add
                result.set(0, (byte) (result.get(0) + 10));
                carry = -1;
            }
        }

        digitCount = 0;
        for (byte r : result) {
            if (r == 0) {
                digitCount++;
            } else {
                break;
            }
        }

        if (digitCount == result.size()) {
            return new BigIntList(0);
        }

        digitCount = result.size() - 1 - digitCount;

        // align from the index to count
        digitCount++;

        // the number of characters used if sign has to put in
        signCount = digitCount + 1;

        // let's assume no sign for now.
        numberCount = digitCount;

        if (add && !thisPositive && !otherPositive) {
            // give an extra space for the sign character
            numberCount = digitCount + 1;
            result.set(result.size() - signCount, (byte) '-');
        }

        return new BigIntList(result.subList(result.size() - numberCount,
                result.size()));
    }

    @Override
    public BigIntList subtract(final BigInt o) {
        return add(o.negate());
    }

    /**
     * Legacy; utterly stupid to use in any scenario. It adds this value bi
     * times to get the result.
     *
     * @param bi the instance to multiply with
     * @return the product
     */
    private BigIntList multiply_with_add(final BigInt bi) {
        BigInt bia = new BigIntList(bi.toString());
        BigInt operand = bia.abs();
        BigIntList result = ZERO;
        for (BigInt i = ZERO; i.compareTo(operand) < 0; i = i.add(ONE)) {
            result = result.add(this);
        }

        if (!bia.isPositive()) {
            result = result.negate();
        }

        return result;
    }

    @Override
    public BigInt multiply(final BigInt o) {
        BigIntList bi = new BigIntList(o.toString());

        /*
         * Prepare yourself; this method is ridiculous.
         *
         * You want recursion? We've got it. You want iterative? We've got it.
         */
        if (this.abs().compareTo(bi.abs()) < 0) {
            // if the absolute value of this is less than other, swap operands
            return bi.multiply(this);
        }

        if (this.compareTo(bi) < 0) {
            // if this value is less than the other,
            // negate operands and negate result
            return this.negate().multiply(bi.negate()).negate();
        }

        // Whether this object is positive
        final boolean thisPositive = isPositive();
        // Whether the object that is being added is positive
        final boolean otherPositive = o.isPositive();

        // We need to know if the signs if the numbers are the same.
        // If they aren't, we need to subtract instead of add
        final boolean samecase = thisPositive == otherPositive;

        final List<List<Byte>> productList = factory.create();

        int carry = 0;
        int count = 0;

        final ListIterator<Byte> thisLi
                = this.backing.listIterator(this.backing.size());

        while (thisLi.hasPrevious()) {
            final ListIterator<Byte> otherLi
                    = bi.backing.listIterator(bi.backing.size());

            final List<Byte> answer = factory.create();
            final byte thisElement = thisLi.previous();

            while (otherLi.hasPrevious()) {
                final byte otherElement = otherLi.previous();

                int result = thisElement * otherElement;
                result += carry;
                carry = result / 10;
                result %= 10;

                answer.add(0, (byte) result);
            }

            if (carry > 0) {
                answer.add(0, (byte) carry);
                carry = 0;
            }

            for (int i = 0; i < count; i++) {
                answer.add((byte) 0);
            }

            count++;

            productList.add(answer);
        }

        List<Byte> fullAnswer = factory.create();
        BigIntList result = ZERO;

        System.out.println(productList);

        for (List<Byte> add : productList) {
            result = result.add(new BigIntList(add));
        }

        return result;
    }

    @Override
    public BigIntList multiply(int o) {
        int operand = Math.abs(o);
        BigIntList result = ZERO;
        for (int i = 0; i < operand; i++) {
            result = result.add(this);
        }

        if (o < 0) {
            result = result.negate();
        }

        return result;
    }

    @Override
    public BigIntList negate() {
        List<Byte> results;
        if (!isPositive()) {
            results = factory.create(backing);
            results.remove(0);
        } else {
            results = factory.create(backing);
            results.add(0, (byte) '-');
        }
        return new BigIntList(results);
    }

    @Override
    public BigIntList abs() {
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
        BigIntList bia = new BigIntList(o.toString());

        final boolean thisPositive = isPositive();
        final boolean otherPositive = bia.isPositive();
        final boolean bothPositive = thisPositive && otherPositive;

        if (thisPositive && !otherPositive) {
            return 1;
        } else if (!thisPositive && otherPositive) {
            return -1;
        }

        if (thisPositive == otherPositive) {
            int result = backing.size() - bia.backing.size();
            result = bothPositive ? result : -result;
            if (result != 0) {
                return result;
            }
        }

        ListIterator<Byte> i = backing.listIterator();
        ListIterator<Byte> j = bia.backing.listIterator();

        while (i.hasNext() && j.hasNext()) {
            final int delta = i.next() - j.next();
            if (delta != 0) {
                return bothPositive ? delta : -delta;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        if (backing.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        int i = 0;
        if (backing.get(0) == '+' || backing.get(0) == '-') {
            sb.append((char) (byte) backing.get(i));
            i++;
        }

        for (; i < backing.size(); i++) {
            sb.append(backing.get(i));
        }

        return sb.toString();
    }

    @Override
    public boolean isPositive() {
        return this.backing.get(0) != '-';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.backing);
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
        final BigIntList other = (BigIntList) obj;
        if (!Objects.equals(this.backing, other.backing)) {
            return false;
        }
        return true;
    }

}
