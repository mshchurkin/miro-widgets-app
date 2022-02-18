package ru.mshchurkin.mirowidgetsapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Widget entity
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

    public Widget(Widget widget) {
        this.id = widget.id;
        this.xCoordinate = widget.xCoordinate;
        this.yCoordinate = widget.yCoordinate;
        this.indexZ = widget.indexZ;
        this.width = widget.width;
        this.height = widget.height;
    }
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer xCoordinate;

    @NotNull
    private Integer yCoordinate;

    @NotNull
    private Integer indexZ;

    @NotNull
    @Positive
    private Integer width;

    @NotNull
    @Positive
    private Integer height;
}
