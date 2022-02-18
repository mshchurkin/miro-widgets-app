package ru.mshchurkin.mirowidgetsapp.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mapper to create dto from model and back
 * 
 * @author Mikhail Shchurkin
 * @created 17.02.2022
 */
@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
