package util;

import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

import model.Endpoint;

public class Path {
    static public Endpoint getEndpoint(HttpExchange exchange,
                                       int length, int numPathParts, String typeTask) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();

        Optional idTaskOpt = IdFrom.request(exchange);

        if (requestMethod.equals("GET") && pathParts.length == 3
                && pathParts[numPathParts].equals(typeTask)) {
            if (idTaskOpt.isEmpty())
                return Endpoint.GET_TASKS;
            else
                return Endpoint.GET_TASK_BY_ID;
        }

        if (requestMethod.equals("POST") && pathParts.length == 3
                && pathParts[numPathParts].equals(typeTask)) {
            if (idTaskOpt.isEmpty())
                return Endpoint.POST_ADD;
            else
                return Endpoint.POST_UPDATE;
        }

        if (requestMethod.equals("DELETE") && pathParts.length == 3
                && pathParts[numPathParts].equals(typeTask)) {
            if (idTaskOpt.isEmpty()) {
                return Endpoint.DELETE_ALL;
            }
            else
                return Endpoint.DELETE_BY_ID;
        }

        return Endpoint.UNKNOWN;
    }

}