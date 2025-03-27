# Unique-ID-Generator
Overview
This project implements a Snowflake ID Generator based on Twitter's Snowflake architecture. Snowflake IDs are unique, time-based, and sortable 64-bit integers designed for distributed systems. Each ID is composed of:

Timestamp (41 bits): Milliseconds since a custom epoch (January 1, 2025)
Machine ID (10 bits): Unique identifier for the generator instance (0-1023)
Sequence number (12 bits): Counter for IDs generated in the same millisecond (0-4095)

Features

Uniqueness: Guarantees unique IDs across multiple nodes/machines
Time-sortable: IDs are sortable by generation time
High Performance: Can generate up to 4,096 unique IDs per millisecond per node
Distributed: Supports up to 1,024 different nodes/machines
Clock Synchronization: Handles clock drift and prevents ID collisions

Project Structure
The project consists of the following Java classes:
SnowFlakeIdGenerator.java
The main implementation of the Snowflake ID algorithm. It handles:

Bit allocation for timestamp, machine ID, and sequence
ID generation with collision prevention
Clock drift detection
Utility methods to extract components from generated IDs

BackwardClockGenerator.java
A subclass of SnowFlakeIdGenerator used for testing clock synchronization issues:

Simulates clock moving backwards
Validates the system's ability to detect and handle time-related edge cases

SnowflakeIdGeneratorTest.java
A comprehensive test suite that verifies:

Basic functionality
ID uniqueness
Clock drift detection and handling
Time ordering of generated IDs
Concurrent ID generation
Multi-node ID generation

Implementation Details
Bit Allocation
The 64-bit ID is divided as follows:

1 bit: Sign bit (always 0 for positive numbers)
41 bits: Timestamp
10 bits: Machine ID
12 bits: Sequence number

Custom Epoch
To maximize the usable timestamp range, a custom epoch of January 1, 2024 (1704067200000 in Unix time) is used instead of the Unix epoch. This allows the generator to create IDs until approximately the year 2055.
Clock Synchronization
The implementation handles cases where the system clock moves backwards (due to NTP adjustments or other issues) by throwing a runtime exception, preventing the generation of non-monotonic IDs.
Testing
The project includes extensive testing for various scenarios:

Basic Test: Verifies ID generation and bit allocation
Uniqueness Test: Ensures uniqueness across thousands of IDs
Clock Test: Validates handling of clock moving backwards
Time Ordering Test: Confirms IDs are sortable by time
Concurrency Test: Verifies thread safety with multiple concurrent generators
Multi-Node Test: Ensures uniqueness across different machine IDs

Run the tests using:
javaCopyjava test.SnowflakeIdGeneratorTest
Performance
The implementation can theoretically generate:

Up to 4,096 IDs per millisecond per node
Up to 4,194,304 IDs per second per node
Across 1,024 nodes: up to 4.3 billion IDs per second

Use Cases
Snowflake IDs are ideal for:

Distributed databases
Microservices architectures
High-volume transaction systems
Event sourcing systems
Any application requiring unique, time-sortable identifiers

Requirements

Java 8 or higher
