package cz.wikimedia.stats.api.controller.dto;

import java.util.Collection;

public record TagDto(String name,
                     Long id,
                     boolean assignable,
                     Long parentId,
                     Collection<Long> childrenIds,
                     Collection<Long> elementIds) {}