package tests;

import org.junit.Test;
import webserver.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by changsuyouk on 07/11/2016.
 */
public class HttpRequestTest {

    private String testDirectory = "/Users/changsuyouk/java/src/tests/Http_GET.txt";

    @Test
    public void request_GET() throws Exception{

        InputStream in = new FileInputStream(new File(testDirectory));

        HttpRequest request = new HttpRequest(in);

        assertEquals("GET", request.getMethod());
        assertEquals("keep-alive", request.getHeader("Connection"));
        String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36";
        assertEquals(agent, request.getHeader("User-Agent"));
    }

}