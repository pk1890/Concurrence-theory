package lab8.zad3;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {


    public static int getPrice(){
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/")
                .build();
        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return Integer.parseInt(response.header("XProductPrice"));
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Future<Integer> intelectualGetPrice(){
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            int price = getPrice();
            completableFuture.complete(price);
        });
        return completableFuture;
    }

    public static void main(String[] args) {
        long start, end;
        start = System.currentTimeMillis();
        System.out.println(Arrays.toString(IntStream.range(0, 200)
                .map((x) -> getPrice()).toArray()));
        end = System.currentTimeMillis();

        long startFuture, endFuture;

        List<Integer> ret = new ArrayList<>();

        startFuture = System.currentTimeMillis();
        System.out.println(Arrays.toString(IntStream.range(0, 200)
                .mapToObj((x) -> {
                    CompletableFuture<Integer> cf = new CompletableFuture<>();
                    Executors.newCachedThreadPool().submit(() -> {cf.complete(getPrice());});
                    return cf;
                })
                .map((x) -> {
                    try {
                        return x.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).toArray()));
        endFuture = System.currentTimeMillis();

        System.out.println("NAIVE: " + (end-start));
        System.out.println("FUTURE: " + (endFuture-startFuture));

    }
}
