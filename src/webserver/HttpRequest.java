package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by changsuyouk on 07/11/2016.
 */
public class HttpRequest {

    private String method;
    private String path;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();

    private static int KEY = 0;
    private static int VALUE = 1;
    private static int METHOD = 0;
    private static int PATH = 1;
    private static int PROTOCOL = 2;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            if(line == null) return;

            String[] firstTokens = line.split(" ");
            method = firstTokens[METHOD];
            path = firstTokens[PATH];
            protocol = firstTokens[PROTOCOL];

            while(!line.equals("")){
                line = br.readLine();
                if(line == null){break;}
                String[] tokens = line.split(" ");
                String value = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
                headers.put(tokens[KEY].replace(":",""), value);
            }

            Set<String> keys = headers.keySet();
            for ( String key : keys){
                System.out.println(key + " / " + headers.get(key));
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getHeader(String field){
        return headers.get(field);
    }

    public String getParameter(String argument){
        return parameters.get(argument);
    }
}
