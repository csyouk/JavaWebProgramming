package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by changsuyouk on 10/18/16.
 */
public class RequestHandler extends Thread {
    private static int PATH = 1;
    private static int BEHAVIOR = 2;
    private static int COMMAND = 0;
    private static int QUERY = 1;
    private static int KEY = 0;
    private static int VALUE = 1;
    private Socket connection;
    private byte[] body;

    public RequestHandler(Socket connectionSocket){
        this.connection = connectionSocket;
    }

    public void run(){
        // try(resource) 리소스는 반드시 회수를 해야한다.
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()){

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            if(line == null) return;

            String[] tokens = line.split(" ");

            printRequestHandler(br, line);

            if(serveIndexFile(tokens, out)) return;

            analyzeQueryParameter(tokens[QUERY]);






//            byte[] body = "".getBytes();
//
//            if(tokens[PATH].contains(".")){
//                String prefix = "/Users/changsuyouk/java/src/webserver/webapp";
//                body = readFile(prefix + tokens[PATH]);
//            } else {
//                System.out.println(tokens[PATH]);
//                String[] paths = tokens[PATH].split("/");
//                for(String path: paths){
//                    System.out.println("path : " + path);
//                }
//                String[] queryStrings = paths[BEHAVIOR].split("&");
//                queryStrings[COMMAND] = queryStrings[COMMAND].split("\\?")[QUERY];
//                for(String query: queryStrings){
//                    System.out.println("query : " + query);
//                }
////                body = "Success".getBytes();
//            }


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void analyzeQueryParameter(String parameter){
        String[] paths = parameter.split("/");
//        for (String path : paths){
//            System.out.println("path : " + path);
//        }

        String[] tokens = paths[BEHAVIOR].split("\\?");
        if (tokens[COMMAND].equals("create")){
            String[] parameters = tokens[QUERY].split("&");

            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            for (String param : parameters){
                String key = param.split("=")[KEY];
                String value = param.split("=")[VALUE];
                keys.add(key);
                values.add(value);
                System.out.println(key + " : " + value);
            }
        }
    }

    private boolean serveIndexFile(String[] tokens, OutputStream out){
        if(tokens[PATH].equals("/") || tokens[PATH].equals("/index.html")){
            System.out.println("first line : " + tokens[PATH]);
            String indexFile = "/index.html";
            String prefix = "/Users/changsuyouk/java/src/webserver/webapp";
            body = readFile(prefix + indexFile);
            makeResponse(out, body);
            return true;
        }
        return false;
    }

    private void makeResponse(OutputStream out, byte[] body){
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void printRequestHandler(BufferedReader br, String line){
        System.out.println("==== Request Handler ====");
        while(!line.equals("")){
            System.out.println("|  " + line);
            try {
                line = br.readLine();
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        System.out.println("=========================");
        System.out.println("");
    }

    private byte[] readFile(String requestPath){

        byte[] data = null;
        try {
            Path path = Paths.get(requestPath);
            data = Files.readAllBytes(path);
        }catch (IOException e){
            System.out.println("Path : " + requestPath);
            System.out.println("File not found!");
        }
        return data;
    }

    private String parseRequestAndGetRequiredFile(InputStream inputStream){
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[4096];
        try{
            for (int n; (n = inputStream.read(b)) != -1;) {
                sb.append(new String(b, 0, n));
            }
        } catch (IOException e){
            System.out.println("Parse Error");
        }
//        System.out.println("==========");
//        System.out.println(sb.toString().split(" ")[1]);
//        System.out.println("==========");

        String root = sb.toString().split(" ")[1];
        return root;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent){
        try{

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body){
        try {

            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e){

        }
    }
}


// REQUEST HEADER
// GET / HTTP/1.1
// Host: localhost:8080
// Connection: keep-alive
// Cache-Control: max-age=0
// Upgrade-Insecure-Requests: 1
// User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36
// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
// DNT: 1
// Accept-Encoding: gzip, deflate, sdch
// Accept-Language: en-US,en;q=0.8,ko;q=0.6
// Cookie: gsScrollPos=0


