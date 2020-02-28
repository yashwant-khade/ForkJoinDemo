import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static java.lang.System.nanoTime;

public class ForkJoin {

    public static void main(final String[] arguments) throws InterruptedException,
            ExecutionException {

//        int nThreads = Runtime.getRuntime().availableProcessors();
//        System.out.println(nThreads);
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","1");

        int[] numbers = new int[(int) (2 * 1e8)];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }

        Double result;
        Long t1, t2;
        ForkJoinPool forkJoinPool;
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(2);
        result = forkJoinPool.invoke(new Sum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 2 threads");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(2);
        result = forkJoinPool.invoke(new BlockingSum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 2 threads Blocking");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(4);
        result = forkJoinPool.invoke(new Sum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 4 threads");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);


        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(4);
        result = forkJoinPool.invoke(new BlockingSum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 4 threads blocking");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(8);
        result = forkJoinPool.invoke(new Sum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 8 threads");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(8);
        result = forkJoinPool.invoke(new BlockingSum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 8 threads blocking");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(12);
        result = forkJoinPool.invoke(new Sum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 12 threads");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        System.out.println();
        t1 = nanoTime();
        forkJoinPool = new ForkJoinPool(12);
        result = forkJoinPool.invoke(new BlockingSum(numbers, 0, numbers.length));
        t2 = nanoTime();
        System.out.println("For 12 threads blocking");
        //System.out.println("Result=" + result);
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        result = 0d;
        System.out.println();
        t1 = nanoTime();
        for (int i = 0; i < numbers.length; i++) {
            result = result + 1 / numbers[i];
        }
        t2 = nanoTime();
        //System.out.println("Result=" + result);
        System.out.println("For single thread");
        System.out.println("Time taken in ms = " + (t2 - t1) / 1e6);

        if (numbers.length >= 1e6) {
            System.out.println("Data Processed " + numbers.length / 1e6 + " Million");
        } else {
            System.out.println("Data Processed " + numbers.length / 1e3 + "K");
        }

    }

    static class Sum extends RecursiveTask<Double> {
        int low;
        int high;
        int[] array;

        Sum(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        protected Double compute() {

            if (high - low <= 10000) {
                double sum = 0;
                for (int i = low; i < high; ++i)
                    sum = sum + (double) (1 / array[i]);
                return sum;
            } else {
                int mid = low + (high - low) / 2;
                Sum left = new Sum(array, low, mid);
                Sum right = new Sum(array, mid, high);
                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }

    static class BlockingSum extends RecursiveTask<Double> {
        int low;
        int high;
        int[] array;

        BlockingSum(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }


        protected Double compute() {

            if (high - low <= 10000) {
                if (low == 0) {
                    double sum = 0;

                    try {
                        Thread.sleep(100);      
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = low; i < high; ++i)
                        sum = sum + (double) (1 / array[i]);
                    return sum;
                } else {
                    double sum = 0;

                    for (int i = low; i < high; ++i)
                        sum = sum + (double) (1 / array[i]);
                    return sum;
                }
            } else {
                int mid = low + (high - low) / 2;
                BlockingSum left = new BlockingSum(array, low, mid);
                BlockingSum right = new BlockingSum(array, mid, high);
                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }
}