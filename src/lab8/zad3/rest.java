//package lab8.zad3;
//
//public class rest {
//
//    void singleThread(){
//        for (int i = 0; i < 200; i++){
//            MyGETRequest();
//        }
//    }
//    void multiThread(){
//        List<CompletableFuture<String>> l = new ArrayList<>();
//        for (int i = 0; i < 200; i++){
//            l.add(singleTask());
//        }
//        for(var f: l){
//            try { f.get(); }catch(Exception e ){}
//        }
//
//    }
//    CompletableFuture<String> singleTask(){
//        CompletableFuture<String> completableFuture = new CompletableFuture<>();
//
//        Executors.newCachedThreadPool().submit(() -> {
//            String s = MyGETRequest();
//            completableFuture.complete(s);
//            return null;
//        });
//        return completableFuture;
//    }
//    public String MyGETRequest() {
//        try {
//            URL urlForGetRequest = new URL("https://jsonplaceholder.typicode.com/posts/1");
//            String readLine = null;
//            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
//            conection.setRequestMethod("GET");
//            conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
//            int responseCode = conection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(conection.getInputStream()));
//                StringBuffer response = new StringBuffer();
//                while ((readLine = in.readLine()) != null) {
//                    response.append(readLine);
//                }
//                in.close();
//                // print result
//                return response.toString();
//
//            } else {
//                System.out.println("GET NOT WORKED");
//                return "";
//            }
//        }
//        catch (Exception e){
//
//        }
//        return "";
//    }
//    public static void main(String[] args) {
//        new Thread(()->{
//            var start = System.nanoTime();
//            new Rest().multiThread();
//            var end = System.nanoTime();
//            System.out.println("Multi  time: " + (end - start) + " count: 200");
//        }).start();
//        {
//            var start = System.nanoTime();
//            //long count =
//            new Rest().singleThread();
//            var end = System.nanoTime();
//            System.out.println("Single time: " + (end - start) + " count: 200" );
//
//        }
//
//    }
//}
//}
