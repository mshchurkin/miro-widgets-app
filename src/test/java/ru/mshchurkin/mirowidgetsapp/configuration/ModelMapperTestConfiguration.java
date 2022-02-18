package ru.mshchurkin.mirowidgetsapp.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mikail Shchurkin
 * @created 18.02.2022
 */
@TestConfiguration
public class ModelMapperTestConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
