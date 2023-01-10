package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    HttpClient client = HttpClient.newHttpClient();
    URI token = URI.create("http://localhost:8078/register");

    public Long register()  {
        // укажите URL запроса, включая его параметры
        //URI token = URI.create("http://localhost:8078/register");

        // создайте объект, описывающий запрос с необходимой информацией
        HttpRequest request = HttpRequest.newBuilder()
                .uri(token)
                .GET()
                .build();

        HttpResponse<String> responseKey = null;
        try {
            responseKey = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) { // обработка ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + token + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

        return Long.parseLong(responseKey.body());
    }

    public void save(String requestBody)  {
        Long apiToken = register();
        URI save = URI.create("http://localhost:8078/save/mykey?API_TOKEN=" + apiToken);

        HttpRequest requestSave = HttpRequest.newBuilder()
                .uri(save)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> responseSeve = null;

        try {
            responseSeve = client.send(requestSave, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) { // обработка ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + token + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load()  {
        Long apiToken = register();
        URI load = URI.create("http://localhost:8078/load/mykey?API_TOKEN=" + apiToken);
        
        HttpRequest requestLoad = HttpRequest.newBuilder()
                .uri(load)
                .GET()
                .build();

        HttpResponse<String> responseLoad = null;

        try {
            responseLoad = client.send(requestLoad, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) { // обработка ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + token + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return responseLoad.body().toString();
    }

}