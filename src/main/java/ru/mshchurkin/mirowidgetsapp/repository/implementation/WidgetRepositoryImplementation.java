package ru.mshchurkin.mirowidgetsapp.repository.implementation;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.repository.WidgetRepository;

/**
 * Widget repository implementation for working with in memory storage
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@Profile("memory")
@Repository
public class WidgetRepositoryImplementation implements WidgetRepository {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, Widget> widgetsMap = new HashMap<>();


    @Override
    public Page<Widget> findAllOrderByIndexZASC(Pageable pageable) {
        List<Widget> widgets = widgetsMap.values().stream()
            .sorted(Comparator.comparingInt(Widget::getIndexZ))
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .map(Widget::new)
            .collect(Collectors.toList());

        return new PageImpl<>(widgets, pageable, widgetsMap.size());
    }

    @Override
    public Optional<Widget> findById(Long id) {
        Widget widget = widgetsMap.get(id);
        return Optional.ofNullable(widget);
    }

    @Override
    public Optional<Widget> findByIndexZ(Integer zIndex) {
        return widgetsMap
            .values()
            .stream()
            .filter(widget -> widget.getIndexZ().intValue() == zIndex.intValue())
            .map(Widget::new)
            .findAny();
    }
    
    @Override
    public Widget save(Widget widget) {
        
        Long widgetId = widget.getId();
        //if widget is new and id is null, create new id using generator
        if (widgetId == null) {
            widgetId = idGenerator.getAndIncrement();
            widget.setId(idGenerator.getAndIncrement());
            //save new widget
            widgetsMap.put(widgetId, widget);
        } else {
            //replace widget by id
            widgetsMap.replace(widgetId, widget);
        }
        return widget;
    }

    @Override
    public Collection<Widget> saveAll(Collection<Widget> widgets) {
        for (Widget widget : widgets) {
            save(widget);
        }
        return widgets;
    }

    @Override
    public boolean deleteWidget(Long id) {
        Widget widget = widgetsMap.remove(id);
        return widget != null;
    }

    @Override
    public Optional<Integer> getMaximumIndexZ() {
        return widgetsMap
            .values()
            .stream()
            .map(Widget::getIndexZ)
            .max(Comparator.naturalOrder());
    }
}
