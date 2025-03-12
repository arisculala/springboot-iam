package com.users.management.util;

import java.util.concurrent.atomic.AtomicLong;

public class SnowflakeIdGenerator {
    private static final long EPOCH = 1672531200000L; // Custom epoch (Jan 1, 2023)
    private static final long NODE_ID_BITS = 10;
    private static final long SEQUENCE_BITS = 12;
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private final long nodeId;
    private long lastTimestamp = -1L;
    private final AtomicLong sequence = new AtomicLong(0);

    private static volatile SnowflakeIdGenerator instance;

    private SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + MAX_NODE_ID);
        }
        this.nodeId = nodeId;
    }

    public static SnowflakeIdGenerator getInstance(long nodeId) {
        if (instance == null) {
            synchronized (SnowflakeIdGenerator.class) {
                if (instance == null) {
                    instance = new SnowflakeIdGenerator(nodeId);
                }
            }
        }
        return instance;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate ID");
        }

        if (timestamp == lastTimestamp) {
            long seq = (sequence.incrementAndGet()) & MAX_SEQUENCE;
            if (seq == 0) {
                while ((timestamp = System.currentTimeMillis()) <= lastTimestamp) {
                    // Wait for next millisecond
                }
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << (NODE_ID_BITS + SEQUENCE_BITS)) |
                (nodeId << SEQUENCE_BITS) |
                sequence.get();
    }
}
