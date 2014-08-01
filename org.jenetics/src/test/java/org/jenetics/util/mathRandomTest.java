/*
/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
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
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.util;

import static org.jenetics.internal.math.math.random.nextBigInteger;
import static org.jenetics.internal.math.math.random.nextLong;
import static org.jenetics.internal.math.random.toDouble;
import static org.jenetics.internal.math.random.toDouble2;
import static org.jenetics.internal.math.random.toFloat;
import static org.jenetics.internal.math.random.toFloat2;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.jenetics.stat.Histogram;
import org.jenetics.stat.StatisticsAssert;
import org.jenetics.stat.UniformDistribution;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2014-08-01 $</em>
 */
public class mathRandomTest {

	@Test(dataProvider = "nextBigIntegerIntData")
	public void nextBigIntegerInt(int n) {
		final long seed = org.jenetics.internal.math.math.random.seed();
		final Random random = new Random(seed);

		final BigInteger value = nextBigInteger(random, BigInteger.valueOf(n));

		random.setSeed(seed);
		Assert.assertEquals(value.intValueExact(), random.nextInt(n));
	}

	@DataProvider(name = "nextBigIntegerIntData")
	public Object[][] nextBigIntegerIntData() {
		return new Object[][] {
			{1},
			{14},
			{100},
			{10_000_000},
			{100_000_000},
			{Integer.MAX_VALUE}
		};
	}

	@Test(dataProvider = "nextBigIntegerLongData")
	public void nextBigIntegerLong(long n) {
		final long seed = org.jenetics.internal.math.math.random.seed();
		final Random random = new Random(seed);

		final BigInteger value = nextBigInteger(random, BigInteger.valueOf(n));

		random.setSeed(seed);
		Assert.assertEquals(value.longValueExact(), nextLong(random, n));
	}

	@DataProvider(name = "nextBigIntegerLongData")
	public Object[][] nextBigIntegerLongData() {
		return new Object[][] {
			{1L + Integer.MAX_VALUE},
			{14L + Integer.MAX_VALUE},
			{100L + Integer.MAX_VALUE},
			{10_000_000L + Integer.MAX_VALUE},
			{100_000_000L + Integer.MAX_VALUE},
			{Long.MAX_VALUE}
		};
	}

	@Test(dataProvider = "nextBigIntegerData")
	public void nextBigIntegerTest(final String string) {
		final long seed = org.jenetics.internal.math.math.random.seed();
		final Random random = new Random(seed);

		final BigInteger n = new BigInteger(string);
		final BigInteger value = nextBigInteger(random, n);

		Assert.assertTrue(value.compareTo(BigInteger.ZERO) >= 0);
		Assert.assertTrue(value.compareTo(n) < 0);
	}

	@DataProvider(name = "nextBigIntegerData")
	public Object[][] nextBigIntegerData() {
		return new Object[][] {
			{"1000000000000000000000000000000000000"},
			{"10000000000000000000000000000000000000000"},
			{"100000000000000000000000000000000000000000000"},
			{"1000000000000000000000000000000000000000000000000"},
			{"10000000000000000000000000000000000000000000000000000"},
			{"100000000000000000000000000000000000000000000000000000000"}
		};
	}

	@Test
	public void seed() {
		for (int i = 0; i < 100; ++i) {
			final long seed1 = org.jenetics.internal.math.math.random.seed();
			final long seed2 = org.jenetics.internal.math.math.random.seed();
			Assert.assertNotEquals(seed1, seed2);
		}
	}

	@Test
	public void seedLong() {
		for (int i = 0; i < 100; ++i) {
			final long seed1 = org.jenetics.internal.math.math.random.seed(i);
			final long seed2 = org.jenetics.internal.math.math.random.seed(i);
			Assert.assertNotEquals(seed1, seed2);
		}
	}

	@Test
	public void seedBytes() {
		final int length = 123;

		for (int i = 0; i < 100; ++i) {
			final byte[] seed1 = org.jenetics.internal.math.math.random.seedBytes(length);
			final byte[] seed2 = org.jenetics.internal.math.math.random.seedBytes(length);
			Assert.assertFalse(Arrays.equals(seed1, seed2));
		}
	}

	@Test(invocationCount = 5)
	public void toFloat_int() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept((double)toFloat(random.nextInt()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toFloat_long() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept((double)toFloat(random.nextLong()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toDouble_long() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept(toDouble(random.nextLong()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toDouble_int_int() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			final long value = random.nextLong();
			histogram.accept(toDouble((int)(value >>> 32), (int)value));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toFloat2_int() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept((double)toFloat2(random.nextInt()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toFloat2_long() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept((double)toFloat2(random.nextLong()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toDouble2_long() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			histogram.accept(toDouble2(random.nextLong()));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

	@Test(invocationCount = 5)
	public void toDouble2_int_int() {
		final Random random = new LCG64ShiftRandom();
		final Histogram<Double> histogram = Histogram.of(0.0, 1.0, 15);

		for (int i = 0; i < 100000; ++i) {
			final long value = random.nextLong();
			histogram.accept(toDouble2((int)(value >>> 32), (int)value));
		}

		final UniformDistribution<Double> distribution = new UniformDistribution<>(0.0, 1.0);
		StatisticsAssert.assertDistribution(histogram, distribution);
	}

}
