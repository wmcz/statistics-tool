package cz.wikimedia.stats.api.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriBuilder;
import reactor.netty.http.client.PrematureCloseException;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.function.Function;

public abstract class AbstractClient {

    private final int MAXIMUM_RETRIES = 3;

    protected final WebClient client;

    protected AbstractClient(WebClient client) {
        this.client = client;
    }

    protected <T> T getWithRetries(Function<UriBuilder, URI> urifunc, ParameterizedTypeReference<T> type) {
        T res = null;
        int retries = 0;

        do {
            try {
                res = client
                        .get()
                        .uri(urifunc)
                        .retrieve()
                        .bodyToMono(type)
                        .block();
                retries = 0;

            } catch (WebClientRequestException e) {

                if (retries <= MAXIMUM_RETRIES && (e.getCause() instanceof PrematureCloseException || e.getCause() instanceof UnknownHostException)) {
                    // probably not ideal but retrying fixes the problem
                    retries++;
                } else throw e;
            }
        } while (retries > 0);
        return res;
    }
}
