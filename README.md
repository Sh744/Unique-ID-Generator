# Unique-ID-Generator
## Overview
This project implements a Snowflake ID Generator based on Twitter's Snowflake ID System. Each ID is composed of:

- *Sign bit*: Always zero to ensure ID is positive
- *Timestamp*: Epoch used was January 1, 2025
- *Machine ID* : Identifier for the generator 
- *Sequence number*: Counter for IDs generated in the same millisecond

## Features
- Creates unique ID each time its been created by using the format given above.

## Project Structure
The project consists of the following Java classes:

### SnowFlakeIdGenerator.java
Handles the logic of the genration of the ID.

### SnowflakeIdGeneratorTest.java
Test code that verifies:
- Basic functionality(ID generated is less than or equal to 64 bits and ID generated is positive)
- ID uniqueness

Run the tests using:
 javaCopyjava test.SnowflakeIdGeneratorTest

## Requirements
Java 8 or higher

## Resources used when building the project
- https://youtu.be/9duCYioH7RI?si=-55fIRCGOTuc0NF0
- https://en.wikipedia.org/wiki/Snowflake_ID
