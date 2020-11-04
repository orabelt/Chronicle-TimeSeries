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

import net.openhft.orabelt.timeseries.memlayout.BytesStore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peter on 19/02/16.
 * modified 01/11/20
 */
public class InMemoryTimeSeries implements TimeSeries {
    public long segmentSize = 1024 * 1024;

    private final LongColumn ts;
    private final Map<String, BaseColumn> columnMap = new LinkedHashMap<>();
    private final TimeSeries parent;
    private long capacity = 0; // by default ensure for a zero elements, use allocate() for preallocate more
    private long length = 0;

    // TODO
    public InMemoryTimeSeries(BytesLongLookup.Factory t) {
        BytesLongLookup b = t.create();
        ts = createLongColumn(TIMESTAMP, (BytesStore)b, b);
        this.parent = null;
    }

    public InMemoryTimeSeries(TimeSeries parent) {
        ts = parent.getTimestamp();
        if ( ts == null ) throw new IllegalStateException("Timestamp is null");
        this.parent = parent;
    }

    @Override
    public synchronized void allocate(long capacity) {
        if (capacity > this.capacity) {
            for (BaseColumn c : columnMap.values()) {
                c.ensureCapacity(capacity);
            }
        }
        this.capacity = capacity;
    }

    @Override
    public long capacity() { return capacity; }

    @Override
    public long length() { return length; }

    @Override
    public long addIndex(long timeStampMicros) {
        LongColumn ts = getTimestamp();
        if (length >= capacity)
             allocate(length + segmentSize);
        ts.set(length++, timeStampMicros);
        return length;
    }

    @Override
    public long getIndex(long timeStampMicros) {
//        final List<T> list = getList();
//        int low = 0;
//        int high = list.size() - 1;
//
//        while (low <= high) { // binarySearch
//            int mid = (low + high) >>> 1;
//            final T price = list.get(mid);
//            long cmp = getPackedDateAndTime(price.getDate(), price.getTime());
//            if (cmp < packedDateAndTime) low = mid + 1;
//            else if (cmp > packedDateAndTime) high = mid - 1;
//            else if (cmp == packedDateAndTime) return mid; // key found
//        }
        return -1;
    }

    @Override
    public LongColumn getTimestamp() {
        return ts;
    }

    @Override
    public List<String> getColumns() {
        final List<String> columns = new ArrayList<>();
        if (parent != null)
            columns.addAll(parent.getColumns());
        columns.addAll(columnMap.keySet());
        return columns;
    }


    @Override
    public LongColumn createLongColumn(String name, BytesStore store, BytesLongLookup lookup) {
        return (LongColumn) columnMap.computeIfAbsent(name, (n) -> new InMemoryLongColumn(this, n, store, lookup, capacity));
    }

    @Override
    public LongColumn getLongColumn(String name) {
        return (LongColumn) columnMap.get(name);
    }

    @Override
    public DoubleColumn createDoubleColumn(String name, BytesStore store, BytesDoubleLookup lookup) {
        return (DoubleColumn) columnMap.computeIfAbsent(name, (n) -> new InMemoryDoubleColumn(this, n, store, lookup, capacity));
    }

    @Override
    public DoubleColumn getDoubleColumn(String name) {
        return (DoubleColumn) columnMap.get(name);
    }

    @Override
    public <T> Column<T> createColumn(String name, Class<T> tClass) {
        return (Column<T>) columnMap.computeIfAbsent(name, (n) -> new InMemoryColumn<>(this, n, capacity));
    }

    @Override
    public <T> Column<T> getColumn(String name, Class<T> tClass) { return (Column<T>) columnMap.get(name); }

//    OLD @Override
//    public DoubleColumn projectAs(String name, DoubleColumn source) {
//        LongColumn ts = getTimestamp();
//        LongColumn ts2 = source.timeSeries().getTimestamp();
//        DoubleColumn result = allocDoubleColumn(name, source.lookup());
//        long i2 = 0, time2 = ts2.get(0);
//        double v2 = source.get(0);
//        for (long i = 0; i < length(); i++) {
//            long time = ts.get(i);
//            OUTER:
//            if (time > time2) {
//                do {
//                    if (i2 + 1 >= ts2.length()) {
//                        v2 = Double.NaN;
//                        time2 = Long.MAX_VALUE;
//                        break OUTER;
//                    }
//                    time2 = ts2.get(++i2);
//                } while (time > time2);
//                v2 = source.get(i2);
//            }
//            result.set(i, v2);
//        }
//        return result;
//    }
//
//    OLD @Override
//    public LongColumn projectAs(String name, LongColumn source) {
//        LongColumn ts = getTimestamp();
//        LongColumn ts2 = source.timeSeries().getTimestamp();
//        LongColumn result = allocLongColumn(name, source.lookup());
//        long i2 = 0, time2 = ts2.get(0);
//        long v2 = source.get(0);
//        for (long i = 0; i < length(); i++) {
//            long time = ts.get(i);
//            OUTER:
//            if (time > time2) {
//                do {
//                    if (i2 + 1 >= ts2.length()) {
//                        v2 = Long.MIN_VALUE;
//                        time2 = Long.MAX_VALUE;
//                        break OUTER;
//                    }
//                    time2 = ts2.get(++i2);
//                } while (time > time2);
//                v2 = source.get(i2);
//            }
//            result.set(i, v2);
//        }
//        return result;
//    }
}
