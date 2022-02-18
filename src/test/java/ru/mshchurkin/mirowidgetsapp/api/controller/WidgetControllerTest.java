package ru.mshchurkin.mirowidgetsapp.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.dto.WidgetDto;
import ru.mshchurkin.mirowidgetsapp.service.WidgetService;

/**
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@WebMvcTest(value = WidgetController.class)
class WidgetControllerTest {

    private static final String WIDGETS_ENDPOINT = "/api/widgets";
    
    @SpyBean
    private ModelMapper modelMapper;
    
    @MockBean
    private WidgetService widgetService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllWidgets() throws Exception {
        when(widgetService.getAllWidgets(0, 10)).thenReturn(Page.empty());

        mockMvc.perform(get(WIDGETS_ENDPOINT))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenGetRequestToWidgetsAndInvalidPagingParams_thenBadRequest() throws Exception {
        mockMvc.perform(get(WIDGETS_ENDPOINT + "/page=0&size=10"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void testGetWidget() throws Exception {
        Widget widget = createWidget();

        when(widgetService.getWidgetById(any())).thenReturn(Optional.of(widget));

        mockMvc.perform(get(WIDGETS_ENDPOINT + "/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testNoSuchWidget() throws Exception {
        when(widgetService.getWidgetById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(WIDGETS_ENDPOINT + "/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
    
    @Test
    void testCreateWidget() throws Exception {
        WidgetDto widget = createWidgetDto();
        Widget createdWidget = createWidget();

        when(widgetService.createWidget(any())).thenReturn(createdWidget);

        mockMvc.perform(post(WIDGETS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateWidgetBadRequest() throws Exception {
        Widget widget = createWidget();

        mockMvc.perform(post(WIDGETS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void testUpdateWidget() throws Exception {
        WidgetDto widget = createWidgetDto();
        Widget createdWidget = createWidget();

        when(widgetService.updateWidget(any())).thenReturn(createdWidget);

        mockMvc.perform(put(WIDGETS_ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateInvalidWidget() throws Exception {
        WidgetDto widget = createWidgetDto();
        widget.setWidth(-1);

        mockMvc.perform(put(WIDGETS_ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void testDeleteWidget() throws Exception {
        when(widgetService.deleteWidget(any())).thenReturn(true);

        mockMvc.perform(delete(WIDGETS_ENDPOINT + "/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNoSuchWidget() throws Exception {
        when(widgetService.deleteWidget(any())).thenReturn(false);

        mockMvc.perform(delete(WIDGETS_ENDPOINT + "/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
    
    private Widget createWidget(){
        Widget widget = new Widget();
        widget.setId(1L);
        widget.setXCoordinate(1);
        widget.setYCoordinate(1);
        widget.setWidth(100);
        widget.setHeight(100);
        widget.setIndexZ(1);
        return widget;
    }

    private WidgetDto createWidgetDto(){
        WidgetDto widget = new WidgetDto();
        widget.setXCoordinate(1);
        widget.setYCoordinate(1);
        widget.setWidth(100);
        widget.setHeight(100);
        widget.setIndexZ(1);
        return widget;
    }
}
