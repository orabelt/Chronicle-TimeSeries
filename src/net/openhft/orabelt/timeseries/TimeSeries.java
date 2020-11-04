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
import net.openhft.orabelt.timeseries.memlayout.carrier.DoubleArrayByteStore;
import net.openhft.orabelt.timeseries.memlayout.carrier.LongArrayByteStore;

import java.util.List;

/**
 * Created by peter on 19/02/16.
 * modified 01/11/20
 */
public interface TimeSeries {
    String TIMESTAMP = "ts_ts";

    void allocate(long capacity);

    long capacity();

    long length();


    long addIndex(long timeStampMicros);

    long getIndex(long timeStampMicros);

    default long addIndexMillis(long timeStampMillis) { return addIndex(timeStampMillis * 1_000); }

    default long getIndexMillis(long timeStampMillis) { return getIndex(timeStampMillis * 1_000); }


    LongColumn getTimestamp();

    List<String> getColumns();


    LongColumn getLongColumn(String name);

    LongColumn createLongColumn(String name, BytesStore store, BytesLongLookup lookup);

    default LongColumn createLongColumn(String name) {
        BytesStore store = new LongArrayByteStore();
        return createLongColumn(name, store, (BytesLongLookup) store);
    }


    DoubleColumn getDoubleColumn(String name);

    DoubleColumn createDoubleColumn(String name, BytesStore store, BytesDoubleLookup lookup);

    default DoubleColumn createDoubleColumn(String name) {
        BytesStore store = new DoubleArrayByteStore();
        return createDoubleColumn(name, store, (BytesDoubleLookup) store);
    }


    <T> Column<T> getColumn(String name, Class<T> tClass);

    <T> Column<T> createColumn(String name, Class<T> tClass);


    //DoubleColumn projectAs(String name, DoubleColumn source);
    //LongColumn projectAs(String name, LongColumn source);
}
