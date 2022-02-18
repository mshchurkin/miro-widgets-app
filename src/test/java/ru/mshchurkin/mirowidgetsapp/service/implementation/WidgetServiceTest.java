package ru.mshchurkin.mirowidgetsapp.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.repository.WidgetRepository;

/**
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {

    @InjectMocks
    private WidgetServiceImplementation widgetService;

    @Mock
    private WidgetRepository widgetRepository;
    
    @BeforeEach
    void init() {
        widgetService = new WidgetServiceImplementation(widgetRepository);
    }

    @Test
    void testCreateEmptyZIndexWidgetWithOtherWidgets() {
        Widget widget = new Widget();
        int maxZIndex = 3;

        when(widgetRepository.getMaximumIndexZ()).thenReturn(Optional.of(maxZIndex));
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widget);

        ArgumentCaptor<Widget> widgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(widgetRepository, times(1)).save(widgetArgument.capture());
        verify(widgetRepository, times(0)).saveAll(any());

        assertEquals(4, widgetArgument.getValue().getIndexZ());
    }

    @Test
    void testCreateWidgetNoMovingOthers() {
        Widget widget = new Widget();
        widget.setIndexZ(1);

        when(widgetRepository.findByIndexZ(any())).thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widget);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaximumIndexZ();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        assertTrue(widgetsArgument.getValue().isEmpty());
    }

    @Test
    void testCreateWidgetsWithMovingOthersToTheEnd() {
        Widget existingWidget1 = createExistingWidget(1L, 1);
        Widget existingWidget2 = createExistingWidget(2L, 2);
        Widget existingWidget3 = createExistingWidget(3L, 3);
        Widget newWidget = createExistingWidget(4L, 2);

        when(widgetRepository.findByIndexZ(any()))
            .thenReturn(Optional.of(existingWidget2))
            .thenReturn(Optional.of(existingWidget3))
            .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.createWidget(newWidget);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaximumIndexZ();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget1.getId().longValue()).findAny();
        assertTrue(widget1.isEmpty());
        
        Optional<Widget> widget2WithIndexZToBe3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget2.getId().longValue()).findAny();
        assertTrue(widget2WithIndexZToBe3.isPresent());
        assertEquals(3, widget2WithIndexZToBe3.get().getIndexZ());
        
        Optional<Widget> widget3WithIndexToBe4 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget3.getId().longValue()).findAny();
        assertTrue(widget3WithIndexToBe4.isPresent());
        assertEquals(4, widget3WithIndexToBe4.get().getIndexZ());
    }

    @Test
    void testCreateWidgetHasSpaceInOrder() {
        Widget existingWidget1 = createExistingWidget(1L, 1);
        Widget existingWidget2 = createExistingWidget(2L, 2);
        Widget existingWidget3 = createExistingWidget(3L, 4);
        Widget newWidget = createExistingWidget(4L, 2);

        when(widgetRepository.findByIndexZ(any()))
            .thenReturn(Optional.of(existingWidget2))
            .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.createWidget(newWidget);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaximumIndexZ();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue()
            == existingWidget1.getId().longValue()).findAny();
        assertTrue(widget1.isEmpty());
        
        Optional<Widget> widget2WithIndexZToBe3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue()
            == existingWidget2.getId().longValue()).findAny();
        assertTrue(widget2WithIndexZToBe3.isPresent());
        assertEquals(3, widget2WithIndexZToBe3.get().getIndexZ());
        
        Optional<Widget> widget3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget3.getId().longValue()).findAny();
        assertTrue(widget3.isEmpty());
    }
    @Test
    void testCreateEmptyWidget() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.createWidget(null));
    }

    @Test
    void testCreateEmptyZIndexWidgetAndNoOtherWidgets() {
        Widget widget = new Widget();

        when(widgetRepository.getMaximumIndexZ()).thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widget);

        ArgumentCaptor<Widget> widgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(widgetRepository, times(1)).save(widgetArgument.capture());
        verify(widgetRepository, times(0)).saveAll(any());

        assertEquals(0, widgetArgument.getValue().getIndexZ());
    }

    @Test
    void testUpdateWidget() {
        Widget existingWidget1 = createExistingWidget(1L, 1);
        Widget existingWidget2 = createExistingWidget(2L, 2);
        Widget existingWidget3 = createExistingWidget(3L, 3);
        Widget existingWidget4 = createExistingWidget(4L, 4);
        Widget widgetToUpdate = createExistingWidget(3L, 2);
        Widget newWidget = createExistingWidget(3L, 2);

        when(widgetRepository.findByIndexZ(any()))
            .thenReturn(Optional.of(existingWidget2))
            .thenReturn(Optional.of(existingWidget3))
            .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.updateWidget(widgetToUpdate);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaximumIndexZ();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget1.getId().longValue()).findAny();
        assertTrue(widget1.isEmpty());

        Optional<Widget> widget2WithIndexZToBe3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget2.getId().longValue()).findAny();
        assertTrue(widget2WithIndexZToBe3.isPresent());
        assertEquals(3, widget2WithIndexZToBe3.get().getIndexZ());
        
        Optional<Widget> widget3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget3.getId().longValue()).findAny();
        assertTrue(widget3.isEmpty());
        
        Optional<Widget> widget4 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() 
            == existingWidget4.getId().longValue()).findAny();
        assertTrue(widget4.isEmpty());
    }
    
    @Test
    void testInvalidGetWidgetsPageNull() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.getAllWidgets(null, 0));
    }

    @Test
    void testInvalidGetWidgetsSizeNull() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.getAllWidgets(0, null));
    }

    @Test
    void testGetWidgets() {
        when(widgetRepository
            .findAllOrderByIndexZASC(PageRequest.of(0, 10)))
            .thenReturn(Page.empty());

        widgetService.getAllWidgets(0, 10);

        verify(widgetRepository, times(1)).findAllOrderByIndexZASC(any());
    }

    @Test
    void testGetWidgetInvalidRequestNoId() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.getWidgetById(null));
    }

    @Test
    void testNoSuchWidget() {
        assertTrue(widgetService.getWidgetById(anyLong()).isEmpty());
    }

    @Test
    void testGetWidget() {
        Widget widget = new Widget();

        when(widgetRepository.findById(anyLong())).thenReturn(Optional.of(widget));

        assertTrue(widgetService.getWidgetById(anyLong()).isPresent());
    }

    @Test
    void testDeleteWidgetInvalidIdNull() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.deleteWidget(null));
    }

    @Test
    void testDeleteWidgetNoSuchId() {
        widgetService.deleteWidget(anyLong());

        verify(widgetRepository, times(1)).deleteWidget(anyLong());
    }
    
    private Widget createExistingWidget(Long id, Integer zIndex){
        Widget widget = new Widget();
        widget.setId(id);
        widget.setIndexZ(zIndex);
        return widget;
    }
}
