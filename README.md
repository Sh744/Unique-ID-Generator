# Unique-ID-Generator
## Overview
This project implements a Snowflake ID Generator based on Twitter's Snowflake architecture. Snowflake IDs are unique, time-based, and sortable 64-bit integers designed for distributed systems. Each ID is composed of:

Timestamp (41 bits): Epoch used was January 1, 2025
Machine ID (10 bits): Identifier for the generator (0-1023)
Sequence number (12 bits): Counter for IDs generated in the same millisecond (0-4095)

## Features

Uniqueness: Guarantees unique IDs across multiple nodes/machines
Time-sortable: IDs are sortable by generation time
High Performance: Can generate up to 4,096 unique IDs per millisecond per node
Distributed: Supports up to 1,024 different nodes/machines
Clock Synchronization: Handles clock drift and prevents ID collisions

## Project Structure
The project consists of the following Java classes:

### SnowFlakeIdGenerator.java
The main implementation of the Snowflake ID algorithm. It handles:

Bit allocation for timestamp, machine ID, and sequence
ID generation with collision prevention
Clock drift detection
Utility methods to extract components from generated IDs

### BackwardClockGenerator.java
A subclass of SnowFlakeIdGenerator used for testing clock synchronization issues:

Simulates clock moving backwards
Validates the system's ability to detect and handle time-related edge cases

### SnowflakeIdGeneratorTest.java
A comprehensive test suite that verifies:

Basic functionality
ID uniqueness
Clock drift detection and handling
Time ordering of generated IDs
Concurrent ID generation
Multi-node ID generation

### Testing
The project includes extensive testing for various scenarios:

Basic Test: Verifies ID generation and bit allocation
Uniqueness Test: Ensures uniqueness across thousands of IDs
Clock Test: Validates handling of clock moving backwards
Time Ordering Test: Confirms IDs are sortable by time
Concurrency Test: Verifies thread safety with multiple concurrent generators
Multi-Node Test: Ensures uniqueness across different machine IDs

Run the tests using:
javaCopyjava test.SnowflakeIdGeneratorTest

## Requirements

Java 8 or higher
