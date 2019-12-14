package lab8.zad1;
//import sun.awt.X11.XSystemTrayPeer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    public static class State{
        int number;
        boolean is_space;
        public State(int number, boolean is_space){
            this.is_space = is_space;
            this.number = number;
        }
    }

    public static void main(String[] args) {
        String text;
        try {

            text = new Scanner(new File("/home/mleko/IdeaProjects/TW/src/lab8/zad1/text.txt")).nextLine();

            long start, end;
            start = System.nanoTime();
            long words_no = Stream.of(text).map((s) -> s.split(" +").length).reduce(0, Integer::sum);
            end = System.nanoTime();
            System.out.println("[Sequential] Word no: " + words_no + " Time: " + (end - start) / 10000);

//            System.out.println(text.split(" ").length);
//            System.out.println(text.split(" +").length);

            start = System.nanoTime();
            long words_no_parallel = StreamSupport.stream(new WordSpliterator(text), true)
                    .parallel()
                    .reduce(new State(0, false),
                            (State s, Character c) -> {
                                if (c == ' ' && !s.is_space){
                                    s.is_space = true;
                                }
                                if (c != ' ' && s.is_space){
                                    s.is_space = false;
                                    s.number++;
                                }
                                return s;
                            },
                            (state, state2) -> {
                                state.number += state2.number;
                                return state;
                            } ).number;
            end = System.nanoTime();
            System.out.println("[Parallel] Word no: " + words_no + " Time: " + (end - start) / 10000);
            start = System.nanoTime();
            long words_no_2 = Stream.of(text).parallel().map((s) -> s.split(" +").length).reduce(0, Integer::sum);
            end = System.nanoTime();
            System.out.println("[ParallelStream_2] Word no: " + words_no_2 + " Time: " + (end - start) / 10000);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



}
