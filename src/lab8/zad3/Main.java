package lab8.zad3;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
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

    public static CompletableFuture<Integer> intelectualGetPrice(){
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
        List<CompletableFuture<Integer>>l = new ArrayList<>();
        for (int i = 0; i < 200; i++){
            l.add(intelectualGetPrice());
        }
        for(CompletableFuture<Integer> f: l){
            try { ret.add(f.get()); }catch(Exception e ){}
        }
        System.out.println(ret.toString());
        endFuture = System.currentTimeMillis();

        System.out.println("NAIVE: " + (end-start));
        System.out.println("FUTURE: " + (endFuture-startFuture));

    }
}
