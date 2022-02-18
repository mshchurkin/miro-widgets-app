package ru.mshchurkin.mirowidgetsapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;

/**
 * Service for widget management 
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
public interface WidgetService {

    /**
     * get all widgets
     * @param page page numbet
     * @param size page size
     * @return page with widgets
     */
    Page<Widget> getAllWidgets(Integer page, Integer size);

    /**
     * get widget by id
     * @param id widget unique identifier
     * @return widget
     */
    Optional<Widget> getWidgetById(Long id);

    /**
     * create new widget
     * @param Widget widget to create
     * @return created widget
     */
    Widget createWidget(Widget Widget);

    /**
     * update existing widget
     * @param Widget widget to update
     * @return updated widget
     */
    Widget updateWidget(Widget Widget);

    /**
     * delete widget by id
     * @param id widget ident to delete
     * @return delete result
     */
    boolean deleteWidget(Long id);
}
