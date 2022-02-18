package ru.mshchurkin.mirowidgetsapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Widget model for api communication
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@ApiModel("Widget")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WidgetDto {

    @ApiModelProperty(value = "Widget id",
        example = "1",
        position = 1)
    @Null
    private Long id;

    @ApiModelProperty(value = "x axis coordinate",
        example = "10",
        required = true,
        position = 2)
    @NotNull
    private Integer xCoordinate;

    @ApiModelProperty(value = "y axis coordinate",
        example = "10",
        required = true,
        position = 3)
    @NotNull
    private Integer yCoordinate;

    @ApiModelProperty(value = "z index",
        example = "1",
        required = true,
        position = 4)
    private Integer indexZ;

    @ApiModelProperty(value = "width",
        example = "1000",
        required = true,
        position = 5)
    @NotNull
    @Positive
    private Integer width;

    @ApiModelProperty(value = "height",
        example = "1000",
        required = true,
        position = 6)
    @NotNull
    @Positive
    private Integer height;
}
