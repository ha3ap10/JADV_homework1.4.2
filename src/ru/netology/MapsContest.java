package ru.netology;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class MapsContest {

    private Map<Integer, Integer> map;
    private int[] array;
    private long startTime;
    private long endTime;
    private LongAdder writeTime = new LongAdder();
    private LongAdder readTime = new LongAdder();
    private int sum = 0;



    public MapsContest(Map<Integer, Integer> map, int[] array) {
        this.map = map;
        this.array = array;
    }

    public void readTest() {
        startTime = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++) {
            map.get(i);
        }
        endTime = System.currentTimeMillis();
        readTime.add(endTime - startTime);
    }

    public void writeTest() {
        startTime = System.currentTimeMillis();

        for (int i = 0; i < array.length; i++) {
            if (!map.containsKey(i))
                map.put(i, array[i]);
        }

        endTime = System.currentTimeMillis();

        writeTime.add(endTime - startTime);
    }

    public long getWriteTime() {
        return writeTime.sum();
    }

    public long getReadTime() {
        return readTime.sum();
    }
}
