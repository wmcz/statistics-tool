package cz.cvut.fit.wikimetric.api.dto.converter;

import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.business.*;
import cz.cvut.fit.wikimetric.model.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TagConverter {
    private final UserTagService userTagService;
    private final EventService eventService;
    private final UserService userService;
    private final EventTagService eventTagService;

    public TagConverter(UserService userService, UserTagService userTagService, EventService eventService, EventTagService eventTagService) {
        this.userService = userService;
        this.userTagService = userTagService;
        this.eventService = eventService;
        this.eventTagService = eventTagService;
    }

    private <T extends Tag<? extends IdAble<Long>>, S extends AbstractService<T, Long>> T getParent(Long id, S service) {
        if (id == null) return null;
        else            return service.findById(id).orElse(null);
    }

    public <T extends Tag<S>, S extends IdAble<Long>> TagDto toDto(T tag) {

        return new TagDto(tag.getName(),
                          tag.getId(),
                          tag.isAssignable(),
                          tag.getParent() == null ? null : tag.getParent().getId(),
                          ConverterUtils.getIds(tag.getChildren()),
                          ConverterUtils.getIds(tag.getTagged()));
    }

    public UserTag toUserTag(TagDto dto) {
        return new UserTag(dto.id(),
                           dto.name(),
                           dto.assignable(),
                           ConverterUtils.getElems(dto.elementIds(), userService),
                           getParent(dto.parentId(), userTagService),
                           ConverterUtils.getElems(dto.childrenIds(), userTagService));
    }



    public EventTag toEventTag(TagDto dto) {
        return new EventTag(dto.id(),
                            dto.name(),
                            dto.assignable(),
                            ConverterUtils.getElems(dto.elementIds(), eventService),
                            getParent(dto.parentId(), eventTagService),
                            ConverterUtils.getElems(dto.childrenIds(), eventTagService));
    }

    public <T extends Tag<S>, S extends IdAble<Long>> Collection<TagDto> toDto(Collection<T> tags) {
        return tags.stream().map(this::toDto).toList();
    }
}
