package ru.mshchurkin.mirowidgetsapp.repository.implementation;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.repository.DatabaseWidgetRepository;
import ru.mshchurkin.mirowidgetsapp.repository.WidgetRepository;

/**
 * Widget repository implementation for working with SQL storage
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@Profile("db")
@Repository
@RequiredArgsConstructor
public class DatabaseWidgetRepositoryImplementation implements WidgetRepository {

    private final DatabaseWidgetRepository widgetRepository;

    @Override
    public Page<Widget> findAllOrderByIndexZASC(Pageable pageable) {
        return widgetRepository.findAllByOrderByIndexZAsc(pageable);
    }

    @Override
    public Optional<Widget> findByIndexZ(Integer zIndex) {
        return widgetRepository.findByIndexZ(zIndex);
    }

    @Override
    public Optional<Widget> findById(Long id) {
        return widgetRepository.findById(id);
    }
    
    @Override
    public Widget save(Widget widget) {
        return widgetRepository.save(widget);
    }
    
    @Override
    public Collection<Widget> saveAll(Collection<Widget> widgets) {
        return widgetRepository.saveAll(widgets);
    }

    @Override
    public boolean deleteWidget(Long id) {
        widgetRepository.deleteById(id);
        return !widgetRepository.existsById(id);
    }
    
    @Override
    public Optional<Integer> getMaximumIndexZ() {
        return widgetRepository.getMaximumIndexZ();
    }
    
}
