package com.alibaba.fastjson2.time;

import com.alibaba.fastjson2.util.IOUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public final class Instant {
    public static final Instant EPOCH = new Instant(0, 0);
    private static final long MIN_SECOND = -31557014167219200L;
    private static final long MAX_SECOND = 31556889864403199L;

    static final long NANOS_PER_SECOND = 1000_000_000L;

    public static Instant ofEpochMilli(long epochMilli) {
        long secs = IOUtils.floorDiv(epochMilli, 1000);
        int mos = (int) IOUtils.floorMod(epochMilli, 1000);
        return create(secs, mos * 1000_000);
    }

    public static Instant of(Date date) {
        return ofEpochMilli(date.getTime());
    }

    public static Instant of(Timestamp date) {
        long millis = date.getTime();
        long second = millis / 1000;
        return ofEpochSecond(second, date.getNanos());
    }

    public static Instant of(Time date) {
        return ofEpochMilli(date.getTime());
    }

    public static Instant ofEpochSecond(long epochSecond, long nanoAdjustment) {
        long secs = IOUtils.addExact(epochSecond, IOUtils.floorDiv(nanoAdjustment, NANOS_PER_SECOND));
        int nos = (int) IOUtils.floorMod(nanoAdjustment, NANOS_PER_SECOND);
        return create(secs, nos);
    }

    public final long epochSecond;
    public final int nanos;

    private Instant(long epochSecond, int nanos) {
        this.epochSecond = epochSecond;
        this.nanos = nanos;
    }

    private static Instant create(long seconds, int nanoOfSecond) {
        if ((seconds | nanoOfSecond) == 0) {
            return EPOCH;
        }

        if (seconds < MIN_SECOND || seconds > MAX_SECOND) {
            throw new DateTimeException("Instant exceeds minimum or maximum instant");
        }

        return new Instant(seconds, nanoOfSecond);
    }

    public long toEpochMilli() {
        if (epochSecond < 0 && nanos > 0) {
            long millis = IOUtils.multiplyExact(epochSecond + 1, 1000);
            long adjustment = nanos / 1000_000 - 1000;
            return IOUtils.addExact(millis, adjustment);
        } else {
            long millis = IOUtils.multiplyExact(epochSecond, 1000);
            return IOUtils.addExact(millis, nanos / 1000_000);
        }
    }

    public Timestamp toTimestamp() {
        Timestamp ts = new Timestamp(toEpochMilli());
        ts.setNanos(nanos);
        return ts;
    }

    public Date toDate() {
        return new Date(toEpochMilli());
    }

    public String toString() {
        return "Instant(" + epochSecond + ", " + nanos + ")";
    }
}
