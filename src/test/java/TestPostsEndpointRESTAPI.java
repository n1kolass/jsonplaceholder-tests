
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Testing https://jsonplaceholder.typicode.com
 * JSONPlaceholder is a free online REST service that you can use whenever you need some fake data.
 * You can refer to the website for the API documentation and examples.
 */

public class TestPostsEndpointRESTAPI {

    private class Post {
        private int userId;
        private int id;
        private String title;
        private String body;
    }

    private void printResponseEntity(CloseableHttpResponse response, boolean isOneElement) throws IOException {
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);
                if (isOneElement) {
                    Post post = gson.fromJson(reader, Post.class);
                    System.out.println("The body is:");
                    System.out.println(gson.toJson(post));
                } else {
                    Post[] posts = gson.fromJson(reader, Post[].class);
                    System.out.println("The first part of body is:");
                    System.out.println(gson.toJson(posts[0]));
                }
            } else {
                System.out.println("Response got no entity.");
            }
        } finally {
            response.close();
        }
    }

    /**
     * Testing reception of the full list of posts.
     * Testing request: GET /posts HTTP/1.1
     * Response status code must be "200 OK".
     */
    @Test
    public void testGetPosts() throws IOException {

        // HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts");
        HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts");
        System.out.println("Executing request: " + request.getRequestLine());

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println("Response is: " + httpResponse.getStatusLine());
        printResponseEntity(httpResponse, false);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /**
     * Testing reception of the first post (post id = 1).
     * Testing request: GET /posts/1 HTTP/1.1
     * Response status code must be "200 OK".
     */
    @Test
    public void testGetPostsWithPostNumber1() throws IOException {

        HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts/1");
        System.out.println("Executing request: " + request.getRequestLine());

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println("Response is: " + httpResponse.getStatusLine());
        printResponseEntity(httpResponse, true);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /**
     * Testing reception of the last post (post id = 100).
     * Testing request: GET /posts/100 HTTP/1.1
     * Response status code must be "200 OK".
     */
    @Test
    public void testGetPostsWithPostNumber100() throws IOException {

        HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts/100");
        System.out.println("Executing request: " + request.getRequestLine());

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println("Response is: " + httpResponse.getStatusLine());
        printResponseEntity(httpResponse, true);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

//    /**
//     * Testing request: GET /posts/101 HTTP/1.1
//     * Response status code must be "404 Not Found".
//     */
//    @Test
//    public void testGetPostsWithPostNumber101() throws IOException {
//
//        HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts/101");
//        System.out.println("Executing request: " + request.getRequestLine());
//
//        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
//        System.out.println("Response is: " + httpResponse.getStatusLine());
//        assertThat(
//                httpResponse.getStatusLine().getStatusCode(),
//                equalTo(HttpStatus.SC_NOT_FOUND));
//    }

    /**
     * Testing reception of the <id> userId posts.
     * Testing request: GET /posts?userId=<id> HTTP/1.1
     * with <id> = 1
     * Response status code must be "200 OK".
     */
    @Test
    public void testGetPostsWithUserIdParameterEqualTo1() throws IOException {

        HttpUriRequest request = new HttpGet("https://jsonplaceholder.typicode.com/posts?userId=1");
        System.out.println("Executing request: " + request.getRequestLine());

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println("Response is: " + httpResponse.getStatusLine());
        printResponseEntity(httpResponse,false);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /**
     * Testing reception of the <id> userId posts.
     * Testing request: GET /posts?userId=<id>&title=<title> HTTP/1.1
     * with <id> = 1 and <title> = "qui est esse"
     * Response status code must be "200 OK".
     */
    @Test
    public void testGetPostsWithUserIdParameterEqualTo1AndSpecificTitle() throws IOException, URISyntaxException {

        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost("jsonplaceholder.typicode.com")
                .setPath("/posts")
                .setParameter("userId", "1")
                .setParameter("title", "qui est esse")
                .build();
        HttpUriRequest request = new HttpGet(uri);
        System.out.println("Executing request: " + request.getRequestLine());

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        System.out.println("Response is: " + httpResponse.getStatusLine());
        printResponseEntity(httpResponse,false);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }
}
