package ru.mshchurkin.mirowidgetsapp.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.violations.ConstraintViolationProblem;
import ru.mshchurkin.mirowidgetsapp.api.exception.WidgetNotFoundException;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;
import ru.mshchurkin.mirowidgetsapp.dto.WidgetDto;
import ru.mshchurkin.mirowidgetsapp.service.WidgetService;

/**
 * Controller for managing widgets
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@RequestMapping(value = "/api/widgets")
@RestController
@RequiredArgsConstructor
@Validated
public class WidgetController {
    
    private final WidgetService widgetService;
    private final ModelMapper modelMapper;

    @ApiOperation("Create widget")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Widget created"),
        @ApiResponse(code = 400, message = "Check widget data to create", response = ConstraintViolationProblem.class)
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WidgetDto> createWidget(@Valid @RequestBody WidgetDto widgetDto) {
        Assert.notNull(widgetDto, "Widget DTO must not be null");
        Widget createdWidget = widgetService.createWidget(mapToEntity(widgetDto));
        return new ResponseEntity<>(mapToDto(createdWidget), HttpStatus.CREATED);
    }

    @ApiOperation("Get page of widgets")
    @ApiResponses({ @ApiResponse(code = 200, message = "Widgets found") })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<WidgetDto>> getAllWidgets(
        @RequestParam(defaultValue = "0") @Min(0) Integer page,
        @RequestParam(defaultValue = "10") @Min(0) Integer size
    ) {
        return new ResponseEntity<>(mapToDto(widgetService.getAllWidgets(page, size)), HttpStatus.OK);
    }

    @ApiOperation("Get widget by id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Widget found"),
        @ApiResponse(code = 404, message = "Widget not found", response = WidgetNotFoundException.class)
    })
    @GetMapping(value = "/{id}", 
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WidgetDto> getWidgetById(@PathVariable Long id) {

        Optional<Widget> widgetOptional = widgetService.getWidgetById(id);
        if (widgetOptional.isPresent()) {
            return new ResponseEntity<>(mapToDto(widgetOptional.get()), HttpStatus.OK);
        }
        throw new WidgetNotFoundException(id);
    }

    @ApiOperation("Update widget by id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Widget updated"),
        @ApiResponse(code = 400, message = "Check widget data to update", response = ConstraintViolationProblem.class)}
    )
    @PutMapping(value = "/{id}", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WidgetDto> updateWidget(@PathVariable Long id, @Valid @RequestBody WidgetDto widgetDto) {
        widgetDto.setId(id);
        return new ResponseEntity<>(mapToDto(widgetService.updateWidget(mapToEntity(widgetDto))), HttpStatus.OK);
    }

    @ApiOperation("Delete widget")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Widget deleted"),
        @ApiResponse(code = 404, message = "Widget not found", response = WidgetNotFoundException.class)
    })
    @DeleteMapping(value = "/{id}", 
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWidget(@PathVariable Long id) {
        boolean deleted = widgetService.deleteWidget(id);
        if (!deleted) {
            throw new WidgetNotFoundException(id);
        }
    }

    private Widget mapToEntity(WidgetDto dto){
        return modelMapper.map(dto, Widget.class);
    }
    
    private WidgetDto mapToDto(Widget widget){
        return modelMapper.map(widget, WidgetDto.class);
    }

    private Page<WidgetDto> mapToDto(Page<Widget> widgets){
       List<WidgetDto> widgetDtoList = 
           widgets
               .getContent()
               .stream()
               .map(widget -> modelMapper.map(widget, WidgetDto.class))
               .collect(Collectors.toList());
        return new PageImpl<>(widgetDtoList, widgets.getPageable(), widgets.getTotalElements());
    }
}
