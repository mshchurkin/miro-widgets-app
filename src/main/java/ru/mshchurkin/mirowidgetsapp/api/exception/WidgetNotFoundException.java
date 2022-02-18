package ru.mshchurkin.mirowidgetsapp.api.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Exception throwing when widget not found
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
public class WidgetNotFoundException extends AbstractThrowableProblem {
    
    public WidgetNotFoundException(Long id) {
        super(null,//TODO 404 page may be?
            "Not found",
            Status.NOT_FOUND,
            String.format("Widget with id: %d not found", id));
    }

}
