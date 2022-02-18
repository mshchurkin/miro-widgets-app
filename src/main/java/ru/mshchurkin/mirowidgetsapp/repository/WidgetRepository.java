package ru.mshchurkin.mirowidgetsapp.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;

/**
 * In memory widget storage
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
public interface WidgetRepository {
    
    Page<Widget> findAllOrderByIndexZASC(Pageable pageable);

    Optional<Widget> findByIndexZ(Integer zIndex);

    Optional<Widget> findById(Long id);

    Widget save(Widget widget);
    
    Collection<Widget> saveAll(Collection<Widget> widgets);

    boolean deleteWidget(Long id);

    Optional<Integer> getMaximumIndexZ();
}
