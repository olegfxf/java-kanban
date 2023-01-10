package util;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.Optional;

public class IdFrom {
    static public Optional<Integer> request(HttpExchange exchange) {
        URI requestedUri = exchange.getRequestURI();

        if (requestedUri.toString().contains("id=") ) {
            int indexId = requestedUri.toString().indexOf("id=") + "id=".length();
            return Optional.of(Integer.parseInt(requestedUri
                    .toString().substring(indexId)));
        }
        else
            return Optional.empty();
    }
}
