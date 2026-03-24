package hse.java.lectures.lecture6.tasks.synchronizer;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizerTest {

    private record Capture(PrintStream stream, ByteArrayOutputStream buf) {
        static Capture create() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            return new Capture(new PrintStream(buf), buf);
        }
        String text() { return buf.toString(); }
    }

    private String run(int n, int ticksPerWriter) {
        Capture cap = Capture.create();
        List<StreamWriter> writers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String letter = String.valueOf((char) ('A' + i));
            writers.add(new StreamWriter(i + 1, letter, cap.stream(), () -> {}));
        }
        new Synchronizer(writers, ticksPerWriter).execute();
        return cap.text();
    }

    private static String buildCycle(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append((char) ('A' + i));
        return sb.toString();
    }

    @Test
    void classicAbcTen() {
        assertEquals("ABCABCABCABCABCABCABCABCABCABC", run(3, 10));
    }

    @ParameterizedTest(name = "n={0}, ticks={1}")
    @CsvSource({"2, 1", "2, 5", "4, 3", "5, 7", "10, 4"})
    void repeatingPattern(int n, int ticks) {
        assertEquals(buildCycle(n).repeat(ticks), run(n, ticks));
    }

    @Test

    void totalLength() {
        assertEquals(4 * 6, run(4, 6).length());
    }

    @Test

    void reverseOrderInput() {
        Capture cap = Capture.create();
        List<StreamWriter> writers = List.of(
                new StreamWriter(3, "C", cap.stream(), () -> {}),
                new StreamWriter(2, "B", cap.stream(), () -> {}),
                new StreamWriter(1, "A", cap.stream(), () -> {})
        );
        new Synchronizer(writers, 5).execute();
        assertEquals("ABCABCABCABCABC", cap.text());
    }

    @Test

    void onTickCallCount() {
        int n = 3, ticks = 8;
        Capture cap = Capture.create();
        AtomicInteger[] counters = new AtomicInteger[n];
        List<StreamWriter> writers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            counters[i] = new AtomicInteger();
            final int idx = i;
            writers.add(new StreamWriter(
                    i + 1, String.valueOf((char) ('A' + i)),
                    cap.stream(), () -> counters[idx].incrementAndGet()
            ));
        }
        new Synchronizer(writers, ticks).execute();
        for (int i = 0; i < n; i++) {
            assertEquals(ticks, counters[i].get(), "writer " + (i + 1) + " tick count");
        }
    }

    @Test

    void twoWritersSingleTick() {
        assertEquals("AB", run(2, 1));
    }

    @Test

    void noDeadlockUnderLoad() throws InterruptedException {
        long[] elapsed = {0};
        Thread t = new Thread(() -> {
            long start = System.currentTimeMillis();
            run(8, 50);
            elapsed[0] = System.currentTimeMillis() - start;
        });
        t.start();
        t.join(5_000);
        assertFalse(t.isAlive(), "deadlock detected — thread still alive after 5s");
        System.out.println("Completed in " + elapsed[0] + " ms");
    }
}