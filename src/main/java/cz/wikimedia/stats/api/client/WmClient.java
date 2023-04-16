package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.api.client.dto.*;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.time.Instant;
import java.util.function.Function;

public class WmClient {
    private final WebClient client;

    private UriBuilder getdefaultQueryParams(UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("action",        "query")
                .queryParam("format",        "json")
                .queryParam("formatversion", 2)
                .queryParam("maxlag",        3);
    }

    private <T> T get(Function<UriBuilder, UriBuilder> params, ParameterizedTypeReference<T> type) {
        return client
                .get()
                .uri(uriBuilder -> params.apply(getdefaultQueryParams(uriBuilder)).build())
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    public WmClient(String projectUrl, BuildProperties properties) {
        this.client = WebClient
                .builder()
                .defaultHeader("User-Agent", properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")")
                .baseUrl("https://" + projectUrl + "/w/api.php")
                .build();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(String username) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("guiuser", username),
                new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {});
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(Long globalUserId) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("guiid", globalUserId),
                new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {});
    }

    public BatchResponse<LocalUserQuery> getUsersById(String localIds) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "users")
                        .queryParam("ususerids", localIds),
                new ParameterizedTypeReference<BatchResponse<LocalUserQuery>>() {});
    }

    public BatchResponse<LocalUserQuery> getUsersByName(String names) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "users")
                        .queryParam("ususers", names),
                new ParameterizedTypeReference<BatchResponse<LocalUserQuery>>() {});
    }


    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getUserContribs(String names, Instant start, Instant end) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uclimit", 200),
                new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {});
    }

    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getMoreUserContribs(String names, Instant start, Instant end, String ucContinue) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uccontinue", ucContinue)
                        .queryParam("uclimit", 200),
                new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {});
    }

    public BatchResponse<PageQuery> getRevInfo(String revIds) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("prop", "revisions")
                        .queryParam("rvprops", "ids")
                        .queryParam("rvslots", "*")
                        .queryParam("revids", revIds),
        new ParameterizedTypeReference<BatchResponse<PageQuery>>() {});
    }

    public BatchResponse<PageQuery> getRevInfoWithSizes(String revIds) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("prop", "revisions")
                        .queryParam("rvprop", "ids|size")
                        .queryParam("rvslots", "*")
                        .queryParam("revids", revIds),
                new ParameterizedTypeReference<BatchResponse<PageQuery>>() {});
    }
}
