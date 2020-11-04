/*
 *
 *  *     Copyright (C) 2016  higherfrequencytrading.com
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Lesser General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Lesser General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Lesser General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.openhft.orabelt.timeseries;

import net.openhft.orabelt.timeseries.memlayout.BytesDoubleLookups;
import net.openhft.orabelt.timeseries.memlayout.BytesLongLookups;
import net.openhft.orabelt.timeseries.memlayout.BytesStore;
import net.openhft.orabelt.timeseries.memlayout.carrier.LongArrayByteStore;
import net.openhft.orabelt.timeseries.memlayout.carrier.UnsafeBytesStore;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by peter on 19/02/16.
 * modified 01/11/20
 */
public class InMemoryTimeSeriesTest {

    public static void main(String args[]) throws Exception {
        new InMemoryTimeSeriesTest().testBidAsk();
    }

    //@Test
    public void testBidAsk() throws ExecutionException, InterruptedException {
        TimeSeries ts = new InMemoryTimeSeries(LongArrayByteStore::new);
        LongColumn time = ts.getTimestamp();

        BytesStore[] store = {new UnsafeBytesStore(), new UnsafeBytesStore(), new UnsafeBytesStore(), new UnsafeBytesStore()};
        //BytesStore[] store = {new DirectBufferBytesStore(), new DirectBufferBytesStore(), new DirectBufferBytesStore(), new DirectBufferBytesStore()};
        //BytesStore[] store = {new UnsafeBytesStoreVolatile(), new UnsafeBytesStoreVolatile(), new UnsafeBytesStoreVolatile(), new UnsafeBytesStoreVolatile()};
        //DoubleColumn bid = ts.createDoubleColumn("bid", store[0], BytesDoubleLookups.FLOAT64);
        //DoubleColumn ask = ts.createDoubleColumn("ask", store[1], BytesDoubleLookups.FLOAT64);
        //LongColumn spread = ts.createLongColumn("spread", store[2], BytesLongLookups.INT64);
        DoubleColumn bid = ts.createDoubleColumn("bid", store[0], BytesDoubleLookups.INT16_4);
        DoubleColumn ask = ts.createDoubleColumn("ask", store[1], BytesDoubleLookups.INT16_4);
        LongColumn spread = ts.createLongColumn("spread", store[2], BytesLongLookups.INT16);
        //DoubleColumn bid = ts.createDoubleColumn("bid");
        //DoubleColumn ask = ts.createDoubleColumn("ask");
        //LongColumn spread = ts.createLongColumn("spread");

        final long size = 100_000_000; //~800Mb per item, use -Xms4096m -Xmx4096m and windows swap
        ts.allocate(size);

        int threads = Runtime.getRuntime().availableProcessors() * 2 - 1;
        //threads = 1;
        long block = (((((size + threads - 1) / threads) - 1) | 63) + 1);

        List<ForkJoinTask<?>> tasks = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            final int finalI = i;
            tasks.add(ForkJoinPool.commonPool().submit(() -> {
                Random rand = ThreadLocalRandom.current();
                long first = finalI * block;
                for (int j = 0, max = (int) Math.min(size - first, block); j < max; j++) {
                    long v = first + j;
                    time.set(v, v);
                    int r1 = rand.nextInt(1000);
                    int r2 = rand.nextInt(1000);
                    bid.set(v, Math.min(r1, r2) / 1000d);
                    ask.set(v, Math.max(r1, r2) / 1000d);
                    spread.set(v, r1 - r2);
                }
            }));
        }
        for (ForkJoinTask<?> task : tasks) {
            task.get();
        }

        long took = System.currentTimeMillis() - start;
        System.out.printf("%d set threads took %.3f secs, %d ops/mics  %n", threads, took / 1000d, size/took/1000);


        int residuals = 0;
        start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            long v = time.get(i);
            double r1 = bid.get(i);
            double r2 = ask.get(i);
            long r = spread.get(i);

            if (v != i) residuals++;
            if (Math.abs(r1 * 1000 - r2 * 1000) > Math.abs(r)) residuals++;
        }

        took = System.currentTimeMillis() - start;
        System.out.printf("%d get threads took %.3f secs, %d ops/mics, residuals: %d %n", 1, took / 1000d, size/took/1000, residuals);

        for (BytesStore s : store)
            if (s instanceof Closeable)
                try { ((Closeable) s).close(); } catch (IOException e) { e.printStackTrace(); } finally {
                    System.out.print(s.getClass().getSimpleName() + ":close(" + s.getAllocatedBytes() + "), ");
                }

        System.out.print("\nDone.");
    }


    //    OLD @Test
    //    public void testGenerateBrownian() {
    //        TimeSeries ts = new InMemoryTimeSeries(null);
    //        long size = 4L << 30;
    //        ts.setLength(size);
    //
    //        long start = System.currentTimeMillis();
    //        DoubleColumn mid = ts.allocDoubleColumn("mid", BytesDoubleLookups.INT16_4);
    //        mid.generateBrownian(1, 2, 0.001);
    //
    //        DoubleColumn spread = ts.allocDoubleColumn("spread", BytesDoubleLookups.INT16_4);
    //        spread.generateBrownian(0.001, 0.001, 0.0001);
    //        long took = System.currentTimeMillis() - start;
    //
    //        System.out.printf("generateBrownian took %.3f secs%n", took / 1e3);
    //    }
    //
    //    OLD @Test
    //    @Ignore("TODO FIX")
    //    public void testGenerateRandomSequence() {
    //        long size = 600_000_000L;
    //
    //        // generate series 1
    //        TimeSeries ts = new InMemoryTimeSeries(null);
    //        ts.setLength(size);
    //
    //        LongColumn time = ts.getTimestamp();
    //        time.setAll(Random::new, (c, i, r) -> c.set(i, 9 + (int) Math.pow(1e6, sqr(r.nextFloat()))));
    //        long sum = time.integrate(); // sum all the intervals
    //
    //        System.out.printf("%.1f days%n", sum / 86400e6);
    //
    //        DoubleColumn mid = ts.allocDoubleColumn("mid", BytesDoubleLookups.INT16_4);
    //        mid.generateBrownian(1, 2, 0.0005);
    //
    //        // generate series 2
    //        TimeSeries ts2 = new InMemoryTimeSeries(null);
    //        ts2.setLength(size);
    //
    //        LongColumn time2 = ts.getTimestamp();
    //        time2.setAll(Random::new, (c, i, r) -> c.set(i, 9 + (int) Math.pow(1e6, sqr(r.nextFloat()))));
    //        long sum2 = time2.integrate(); // sum all the intervals
    //
    //        System.out.printf("%.1f days%n", sum2 / 86400e6);
    //
    //        DoubleColumn mid2 = ts2.allocDoubleColumn("mid", BytesDoubleLookups.INT16_4);
    //        mid2.generateBrownian(1, 2, 0.0005);
    //
    //        // compare the correlation
    ////    CorrelationStatistic stats = PearsonsCorrelation.calcCorrelation(mid, mid2, Mode.AFTER_BOTH_CHANGE);
    //    }
}
