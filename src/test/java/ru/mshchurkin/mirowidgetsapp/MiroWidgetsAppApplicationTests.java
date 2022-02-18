package ru.mshchurkin.mirowidgetsapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import ru.mshchurkin.mirowidgetsapp.api.controller.WidgetController;

@ActiveProfiles("memory")
@SpringBootTest
class MiroWidgetsAppApplicationTests {

	@SpyBean
	private ModelMapper modelMapper;
	
	@Autowired
	private WidgetController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
