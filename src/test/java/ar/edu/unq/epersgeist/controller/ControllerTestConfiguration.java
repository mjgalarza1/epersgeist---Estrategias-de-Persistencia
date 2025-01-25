package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.rest.EspirituREST;
import ar.edu.unq.epersgeist.controller.rest.GlobalControllerAdviser;
import ar.edu.unq.epersgeist.controller.rest.MediumREST;
import ar.edu.unq.epersgeist.controller.rest.UbicacionREST;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Configuration
@ComponentScan(basePackages = {"ar.edu.unq.epersgeist"})
public class ControllerTestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    // Spring va a intentar hacer autowiring de toda dependencia que declaremos
    // como parametro en el metodo del Bean.
    public MockMvc mockMvc(EspirituREST espirituREST, MediumREST mediumREST, UbicacionREST ubicacionREST, GlobalControllerAdviser globalControllerAdviser) {
        return MockMvcBuilders.standaloneSetup(espirituREST, mediumREST, ubicacionREST, globalControllerAdviser).build();
    }
}
