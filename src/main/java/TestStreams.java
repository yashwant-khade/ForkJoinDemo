import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import static java.lang.System.nanoTime;

public class TestStreams {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1e6; i++) {
            l.add(i);
        }
        long t1 = nanoTime();

        OptionalDouble avg = l.parallelStream().mapToInt(x -> x).average();

        long t2 = nanoTime();



        long t3 = nanoTime();

        double result = 0;
        for (int i = 0; i < l.size(); i++) {
            result += l.get(i);
        }
        result=result/l.size();

        long t4 = nanoTime();
        System.out.println(avg);

        System.out.println((t2 - t1) / 1e6);
        System.out.println(result);
        System.out.println((t4 - t3) / 1e6);
    }
}
