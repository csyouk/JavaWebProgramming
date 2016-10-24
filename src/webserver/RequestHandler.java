package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by changsuyouk on 10/18/16.
 */
public class RequestHandler extends Thread {
    private static int PATH = 1;
    private static int BEHAVIOR = 1;
    private Socket connection;

    public RequestHandler(Socket connectionSocket){
        this.connection = connectionSocket;
    }

    public void run(){
        // try(resource) 리소스는 반드시 회수를 해야한다.
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()){

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            System.out.println("line : " + line);
            String[] tokens = line.split(" ");
            byte[] body = "".getBytes();
            if(tokens[PATH].equals("/index.html")){
                String prefix = "/Users/changsuyouk/java/src/webserver/webapp";
                body = readFile(prefix + tokens[PATH]);
            } else {
                System.out.println(tokens[PATH]);
                String[] paths = tokens[PATH].split("/");

                String[] queryStrings = paths[BEHAVIOR].split("&");
                for(String query: queryStrings){
                    System.out.println("query ; " + query);
                }
//                body = "Success".getBytes();
            }

            if(line == null) return;

            while(!line.equals("")){
                line = br.readLine();
                System.out.println("line : " + line);
            }

            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello world".getBytes();

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e){
            e.printStackTrace();
        }
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
