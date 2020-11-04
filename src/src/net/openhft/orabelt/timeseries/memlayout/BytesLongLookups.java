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

package net.openhft.orabelt.timeseries.memlayout;

import net.openhft.orabelt.timeseries.BytesLongLookup;

/**
 * Created by peter on 19/02/16.
 * modified 01/11/20
 */
public enum BytesLongLookups implements BytesLongLookup {
    INT64 {
        @Override
        public long get(BytesStore bytes, long index) {
            return bytes.readLong(index << 3);
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            bytes.writeLong(index << 3, value);
        }

        @Override
        public long sizeFor(long capacity) {
            return capacity << 3;
        }

    },
    INT32 {
        @Override
        public long get(BytesStore bytes, long index) {
            int i = bytes.readInt(index << 2);
            return i == Integer.MIN_VALUE ? Long.MIN_VALUE : i;
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            int i = value == Long.MIN_VALUE ? Integer.MIN_VALUE : Math.toIntExact(value);
            bytes.writeInt(index << 2, i);
        }

        @Override
        public long sizeFor(long capacity) {
            return capacity << 2;
        }

    },
    INT16 {
        @Override
        public long get(BytesStore bytes, long index) {
            short i = bytes.readShort(index << 1);
            return i == Short.MIN_VALUE ? Long.MIN_VALUE : i;
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            short i = value == Long.MIN_VALUE ? Short.MIN_VALUE : Longs.toInt16(value);
            bytes.writeShort(index << 1, i);
        }

        @Override
        public long sizeFor(long capacity) {
            return capacity << 1;
        }
    },
    INT8 {
        @Override
        public long get(BytesStore bytes, long index) {
            return bytes.readByte(index);
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            bytes.writeByte(index, Longs.toInt8(value));
        }

        @Override
        public long sizeFor(long capacity) {
            return capacity;
        }
    },
    UINT8 {
        @Override
        public long get(BytesStore bytes, long index) {
            return bytes.readUnsignedByte(index);
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            bytes.writeUnsignedByte(index, Longs.toUInt8(value));
        }

        @Override
        public long sizeFor(long capacity) {
            return capacity;
        }
    },
    UINT4 {
        @Override
        public long get(BytesStore bytes, long index) {
            int i = bytes.readUnsignedByte(index >> 1);
            return (index & 1) != 0 ? (i >> 4) : (i & 0xF);
        }

        @Override
        public void set(BytesStore bytes, long index, long value) {
            int i = bytes.readUnsignedByte(index >> 1);
            int i2 = (int) ((index & 1) != 0 ? (i & 0xF0) | (value & 0xF) : (i & 0xF) | ((value & 0xf) << 4));
            bytes.writeUnsignedByte(index, Longs.toUInt8(value));
        }

        @Override
        public long sizeFor(long capacity) {
            return (capacity + 1) >> 1;
        }
    }
}
