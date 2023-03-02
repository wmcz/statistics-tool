package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BatchResponse<Q extends Query<?>>(@JsonProperty("batchcomplete") Boolean batchComplete, @JsonProperty("query") Q query) {
}
