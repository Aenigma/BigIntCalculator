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

/**
 * This interface defines the 
 * 
 * @author Kevin Raoofi
 */
public interface BigInt extends Comparable<BigInt> {

    /**
     *
     * @return BigInt representing the absolute value of the instance
     */
    BigInt abs();

    /**
     * Adds two numbers together
     *
     * @param o other BigInt object to add with
     * @return the sum
     */
    BigInt add(BigInt o);

    /**
     * Multiplies two big integer values together
     *
     * @param bi other big integer
     * @return the product
     */
    BigInt multiply(BigInt bi);

    /**
     * Multiplies big integer with a regular int
     *
     * @param o integer to multiply this big integer with
     * @return the product
     */
    BigInt multiply(int o);

    /**
     * Returns this with its sign flipped
     *
     * @return this multiplied by -1
     */
    BigInt negate();

    /**
     * Subtracts this with the given value
     *
     * @param o value to subtract this with
     * @return the difference
     */
    BigInt subtract(BigInt o);

    /**
     * Determines whether the number is positive or not
     *
     * @return true if positive; false if negative. 0 should also return
     * positive.
     */
    boolean isPositive();

}
