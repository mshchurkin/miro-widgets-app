package ru.mshchurkin.mirowidgetsapp.service.implementation;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.repository.WidgetRepository;
import ru.mshchurkin.mirowidgetsapp.service.WidgetService;

/**
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@Transactional
@Service
public class WidgetServiceImplementation implements WidgetService {

    private final Lock writeLock;
    private final Lock readLock;
    private final WidgetRepository widgetRepository;

    public WidgetServiceImplementation(WidgetRepository widgetRepository) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Page<Widget> getAllWidgets(Integer page, Integer size) {
        Assert.notNull(page, "Error: page is null");
        Assert.notNull(size, "Error: size not be null");

        try {
            readLock.lock();
            return widgetRepository.findAllOrderByIndexZASC(PageRequest.of(page, size));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<Widget> getWidgetById(Long id) {
        Assert.notNull(id, "Error: id is null");

        try {
            readLock.lock();
            return widgetRepository.findById(id);
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public Widget createWidget(Widget widget) {
        Assert.notNull(widget, "Error: widget is null");

        try {
            writeLock.lock();
            return saveWidget(widget);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Widget updateWidget(Widget widget) {
        Assert.notNull(widget, "Error: widget is null");

        try {
            writeLock.lock();
            return saveWidget(widget);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean deleteWidget(Long id) {
        Assert.notNull(id, "Error: id is null");
        try {
            writeLock.lock();
            return widgetRepository.deleteWidget(id);
        } finally {
            writeLock.unlock();
        }
    }
    
    private Widget saveWidget(Widget widget) {
        if (widget.getIndexZ() == null) {
            Optional<Integer> maxZIndex = widgetRepository.getMaximumIndexZ();
            int zIndex = 0;
            if (maxZIndex.isPresent()) {
                zIndex = maxZIndex.get() + 1;
            }
            widget.setIndexZ(zIndex);
        } else {
            ArrayList<Widget> newWidgetList = getNewWidgetOrder(widget);
            widgetRepository.saveAll(newWidgetList);
        }
        
        return widgetRepository.save(widget);
    }

    private ArrayList<Widget> getNewWidgetOrder(Widget widgetToPlace) {

        ArrayList<Widget> widgetsToLineUp = new ArrayList<>();
        Integer zIndex = widgetToPlace.getIndexZ();
        
        Optional<Widget> optWidgetToMove = 
            widgetRepository.findByIndexZ(zIndex);
        
        while (optWidgetToMove.isPresent()) {
            Widget widgetToMove = optWidgetToMove.get();
            //if widget to move is the same as widget to place, 
            // just break the loop, no need to reorder widgets
            if (widgetToPlace.getId() != null 
                && widgetToPlace.getId().longValue() == widgetToMove.getId().longValue()) {
                break;
            }
            
            zIndex++;
            optWidgetToMove = widgetRepository.findByIndexZ(zIndex);
            widgetToMove.setIndexZ(zIndex);
            widgetsToLineUp.add(widgetToMove);
        }
        return widgetsToLineUp;
    }
    
}
