/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.hash;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

import java.util.Random;

/**
 * Benchmarks for hashing functions. This class benchmarks various hasing functions for a range of
 * sizes of byte array. The input data is generated by a call to {@link Random#nextBytes}.
 *
 * <p>Parameters for the benchmark are:
 * <ul>
 * <li>size: The length of the byte array to hash.
 * <li>function: The name of the function(s) to test (eg, "goodFastHash32" or "murmur3_32")
 * </ul>
 *
 * @author David Beaumont
 */
public class HashBenchmark extends SimpleBenchmark {

  @Param({"10", "1000", "1000000"})
  private int size;

  @Param private HashType function;

  private enum HashType {
    goodFastHash32() {
      @Override public long hash(byte[] data) {
        return Hashing.goodFastHash(32).hashBytes(data).asInt();
      }
    },
    goodFastHash64() {
      @Override public long hash(byte[] data) {
        return Hashing.goodFastHash(64).hashBytes(data).asLong();
      }
    },
    murmur32() {
      @Override public long hash(byte[] data) {
        return Hashing.murmur3_32().hashBytes(data).asInt();
      }
    },
    murmur128() {
      @Override public long hash(byte[] data) {
        return Hashing.murmur3_128().hashBytes(data).asLong();
      }
    },
    md5() {
      @Override public long hash(byte[] data) {
        return Hashing.md5().hashBytes(data).asLong();
      }
    };
    public abstract long hash(byte[] data);
  }

  private byte[] testData;

  @Override
  protected void setUp() {
    testData = new byte[size];
    new Random().nextBytes(testData);
  }

  public int timeHashFunction(int reps) {
    long dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy ^= function.hash(testData);
    }
    return (int) dummy;
  }

  public static void main(String[] args) {
    Runner.main(HashBenchmark.class, args);
  }
}