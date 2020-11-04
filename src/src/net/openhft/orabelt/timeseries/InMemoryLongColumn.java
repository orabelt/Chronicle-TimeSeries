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

/**
 * Created by peter on 19/02/16.
 * modified 01/11/20
 */
public class InMemoryLongColumn extends AbstractColumn implements LongColumn {
    private final BytesLongLookup lookup;
    private final BytesStore bytes;

    public InMemoryLongColumn(TimeSeries timeSeries, String name, BytesStore store, long capacity) {
        this(timeSeries, name, store, (BytesLongLookup)store, capacity);
    }

    public InMemoryLongColumn(TimeSeries timeSeries, String name, BytesStore store, BytesLongLookup lookup, long capacity) {
        super(timeSeries, name);
        this.lookup = lookup;
        this.bytes = store;

        ensureCapacity(capacity);
    }

    @Override
    public void ensureCapacity(long capacity) {
        long cap = lookup.sizeFor(capacity);
        bytes.ensureCapacity(cap);
    }

    @Override
    public void set(long index, long value) {
//        if (index < 0 || index > bytes.realCapacity())
//            throw new AssertionError("index: " + index);
        lookup.set(bytes, index, value);
    }

    @Override
    public long get(long index) {
        return lookup.get(bytes, index);
    }

    @Override
    public long add(long index, long value) {
        long x = lookup.get(bytes, index);
        x += value;
        lookup.set(bytes, index, x);
        return x;
    }

    @Override
    public BytesLongLookup lookup() {
        return lookup;
    }
}
