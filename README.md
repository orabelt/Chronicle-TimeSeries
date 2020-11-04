# Chronicle-TimeSeries
Multi-Threaded Time Series library

# Purpose
This library has two efficiency objectives

- efficient storage on long sequences of data in a column based database.
- multi-threaded processing where possible
- integration with engine for lookup and management of the TimeSeries.

# Redesigned version
Simple redesign for tests on jdk8-15

# Sample program output
```
15 set threads took 0.376 secs, 266 ops/mics  
1 get threads took 0.260 secs, 384 ops/mics, residuals: 0  
UnsafeBytesStore:close(800000000b), UnsafeBytesStore:close(800000000b), UnsafeBytesStore:close(800000000b), UnsafeBytesStore:close(0b), 
Done.
Process finished with exit code 0
```