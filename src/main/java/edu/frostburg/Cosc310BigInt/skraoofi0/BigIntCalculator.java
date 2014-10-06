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

import edu.frostburg.cosc310.Cosc310BigIntCalculator;

/**
 *
 * @author Kevin Raoofi
 */
public class BigIntCalculator implements Cosc310BigIntCalculator {

    public BigIntCalculator() {
    }

    @Override
    public String add(String a, String b) {
        return new BigIntList(a).add(new BigIntList(b)).toString();
    }

    @Override
    public String subtract(String a, String b) {
        return new BigIntList(a).subtract(new BigIntList(b)).toString();
    }

    @Override
    public String multiply(String a, String b) {
        return new BigIntList(a).multiply(new BigIntList(b)).toString();
    }

}
