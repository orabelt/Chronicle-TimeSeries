# Chronicle-TimeSeries
Multi-Threaded Time Series library

# Purpose
This library has two efficiency objectives

- efficient storage on long sequences of data in a column based database.
- multi-threaded processing where possible
- integration with engine for lookup and management of the TimeSeries.

# Redesigned version
Simple redesign for tests on jdk8-15

JDK 1.8, Java Runtime 8-15

# Sample program output
```
 Warm up
7 set threads took 0,343 secs, 291 ops/mics
1 get threads took 0,275 secs, 363 ops/mics, residuals: 0
UnsafeBytesStore:close(762Mb), UnsafeBytesStore:close(762Mb), UnsafeBytesStore:close(762Mb), UnsafeBytesStore:close(0Mb),
Done.
```