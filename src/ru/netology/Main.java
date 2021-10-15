package ru.netology;

import org.w3c.dom.ls.LSOutput;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

/*
      Collections.synchronizedMap() записывает медленней в 2-2.5 раза, чтение дольше около 10 раз.
      А вот если запустить только один поток, то ConcurrentHashMap получается менее производительным
*/

    public static final int ARRAY_SIZE = 10_000_000;
    public static final int MAX_RANDOM = 5_000;
    public static final int THREADS_COUNT = 1;

    public static void main(String[] args) {

        Map<Integer, Integer> concMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

        int[] array = generateArray();

        MapsContest concMapTest = new MapsContest(concMap, array);
        MapsContest syncMapTest = new MapsContest(syncMap, array);

        long concMapWriteTime = startWriteTest(concMapTest);
        long syncMapWriteTime = startWriteTest(syncMapTest);
        long concMapReadTime = startReadTest(concMapTest);
        long syncMapReadTime = startReadTest(syncMapTest);

        print("Время записи ConcurrentHashMap:", concMapWriteTime);
        print("Время записи synchronizedMap:", syncMapWriteTime);
        print("Время чтения ConcurrentHashMap:", concMapReadTime);
        print("Время чтения synchronizedMap:", syncMapReadTime);

    }

    public static void print(String explanation, long value) {
        System.out.printf("%-33s %,10d\n", explanation, value);
    }

    public static long startWriteTest(MapsContest mapTest) {
        ThreadGroup writeGroup = new ThreadGroup("Write Group");
        Runnable runWriteTest = mapTest::writeTest;

        for (int i = 1; i <= THREADS_COUNT; i++) {
            new Thread(writeGroup, runWriteTest, "Поток записи " + i).start();
        }

        while (writeGroup.activeCount() > 0);

        return mapTest.getWriteTime();
    }

    public static long startReadTest(MapsContest mapTest) {
        ThreadGroup readGroup = new ThreadGroup("Read Group");
        Runnable runReadTest = mapTest::readTest;

        for (int i = 1; i <= THREADS_COUNT; i++) {
            new Thread(readGroup, runReadTest, "Поток чтения " + i).start();
        }

        while (readGroup.activeCount() > 0);

        return mapTest.getReadTime();
    }

    public static int[] generateArray() {
        Random random = new Random();
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(MAX_RANDOM) + 1;
        }
        return array;
    }
}
