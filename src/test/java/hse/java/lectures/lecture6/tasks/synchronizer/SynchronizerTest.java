package hse.java.lectures.lecture6.tasks.synchronizer;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

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


    @Test
    void classicAbcTen() {
        String result = run(3, 10);
        assertThat(result).isEqualTo("ABCABCABCABCABCABCABCABCABCABC");
    }

    @ParameterizedTest(name = "n={0}, ticks={1}")
    @CsvSource({
            "2, 1",
            "2, 5",
            "4, 3",
            "5, 7",
            "10, 4",
    })

    void repeatingPattern(int n, int ticks) {

        StringBuilder expected = new StringBuilder();
        String cycle = buildCycle(n);
        expected.repeat(cycle, ticks);

        String result = run(n, ticks);
        assertThat(result).isEqualTo(cycle.repeat(ticks));
    }

    @Test

    void totalLength() {
        int n = 4, ticks = 6;
        assertThat(run(n, ticks)).hasSize(n * ticks);
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
        assertThat(cap.text()).isEqualTo("ABCABCABCABCABC");
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
                    i + 1,
                    String.valueOf((char) ('A' + i)),
                    cap.stream(),
                    () -> counters[idx].incrementAndGet()
            ));
        }
        new Synchronizer(writers, ticks).execute();
        for (int i = 0; i < n; i++) {
            assertThat(counters[i].get())
                    .as("записал %d тик кол-во", i + 1)
                    .isEqualTo(ticks);
        }
    }

    @Test

    void twoWritersSingleTick() {
        assertThat(run(2, 1)).isEqualTo("AB");
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
        assertThat(t.isAlive()).as(" должно закончиться в течение 5s").isFalse();
        System.out.println("закочено в  " + elapsed[0] + " ms");
    }



    private static String buildCycle(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append((char) ('A' + i));
        return sb.toString();
    }
}