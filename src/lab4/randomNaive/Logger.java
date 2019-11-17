package lab4.randomNaive;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static double[] measurementCountProducers;
    private static double[] timeSumProducers;
    private static double[] measurementCountConsumers;
    private static double[] timeSumConsumers;
    private static int processCount;
    private static int lineSize;

    static void init(int _processCount, int _lineSize) {
        processCount = _processCount;
        lineSize = _lineSize;
        measurementCountProducers = new double[lineSize / 2];
        timeSumProducers = new double[lineSize / 2];
        measurementCountConsumers = new double[lineSize / 2];
        timeSumConsumers = new double[lineSize / 2];
    }

    static synchronized void addMeasurementProducer(double timeDiff, int portionSize) {
        measurementCountProducers[portionSize]++;
        timeSumProducers[portionSize] += timeDiff;
    }

    static synchronized void addMeasurementConsumer(double timeDiff, int portionSize) {
        measurementCountConsumers[portionSize]++;
        timeSumConsumers[portionSize] += timeDiff;
    }

    static synchronized void summary() throws IOException {
        FileWriter writer = new FileWriter(
                "./../data/" + lineSize + "_" + processCount + "_P"
        );

        for (int i = 1; i < lineSize / 2; i++) {
            if (measurementCountProducers[i] == 0) {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(0))
                        .append("\n");
            } else {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(timeSumProducers[i] / measurementCountProducers[i]))
                        .append("\n");
            }
        }
        writer.close();

        writer = new FileWriter(
                "./../data/" + lineSize + "_" + processCount + "_C"
        );

        for (int i = 1; i < lineSize / 2; i++) {
            if (measurementCountConsumers[i] == 0) {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(0))
                        .append("\n");
            } else {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(timeSumConsumers[i] / measurementCountConsumers[i]))
                        .append("\n");
            }
        }

        writer.close();

        writer = new FileWriter(
                "./../data/summary",
                true
        );

        double operation = 0;
        double sumTime = 0;
        for (int i = 0; i < lineSize / 2; i++) {
            operation += measurementCountConsumers[i];
            sumTime += timeSumConsumers[i];
        }

        writer.append(String.valueOf(lineSize))
                .append("_")
                .append(String.valueOf(processCount))
                .append("_C operation: ")
                .append(String.valueOf(operation))
                .append(" time: ")
                .append(String.valueOf(sumTime))
                .append(" average: ")
                .append(String.valueOf(sumTime / operation))
                .append("\n");

        for (int i = 0; i < lineSize / 2; i++) {
            operation += measurementCountProducers[i];
            sumTime += timeSumProducers[i];
        }

        writer.append(String.valueOf(lineSize))
                .append("_")
                .append(String.valueOf(processCount))
                .append("_P operation: ")
                .append(String.valueOf(operation))
                .append(" time: ")
                .append(String.valueOf(sumTime))
                .append(" average: ")
                .append(String.valueOf(sumTime / operation))
                .append("\n");

        writer.close();
    }
}