package com.example.rest_client;

import com.example.rest_client.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpTimeoutException;
import java.util.List;
import java.util.stream.Collectors;

//@SpringBootApplication
public class RestClientApplication {

    static RestTemplate restTemplate = new RestTemplate();

    static String baseUrl = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        //SpringApplication.run(RestClientApplication.class, args);

        HttpHeaders headers = getAllUsersAndSetCookie();
        User user = saveUserId3(headers);
        updateUser(headers, user);
        deleteUser(user, headers);
    }

    public static HttpHeaders getAllUsersAndSetCookie() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl,
                HttpMethod.GET,
                requestEntity,
                String.class);

        HttpHeaders responseHeaders = responseEntity.getHeaders();
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        headers.set("Cookie", String.join(";", cookies));
        System.out.println("response headers: " + responseHeaders);

        System.out.println("USERS:    " + responseEntity.getBody());

        return headers;
    }

    public static User saveUserId3(HttpHeaders headers) {

        User newUser = new User(3L, "James", "Brown", (byte) 15);

        HttpEntity<Object> requestEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        System.out.println("BODY1:    " + responseEntity.getBody());

        System.out.println(getAllUsersAndSetCookie());
        return newUser;
    }

    public static void updateUser(HttpHeaders headers, User user) {
        user.setName("Thomas");
        user.setLastName("Shelby");

        HttpEntity<Object> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl,
                HttpMethod.PUT,
                requestEntity,
                String.class);

        System.out.println("BODY2:    " + responseEntity.getBody());
    }

    public static void deleteUser(User user, HttpHeaders headers) {
        long id = user.getId();

        HttpEntity<Object> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/" + id,
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        System.out.println("BODY3:    " + responseEntity.getBody());
    }
}
