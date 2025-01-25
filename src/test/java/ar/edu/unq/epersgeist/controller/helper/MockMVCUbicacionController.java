package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collection;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@Component
public class MockMVCUbicacionController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }

    public  <T> T crear(CreateUbicacionDTO createUbicacionDTO, HttpStatus expectedStatus,  Class<T> clazz) throws Throwable {
        var json = objectMapper.writeValueAsString(createUbicacionDTO);

        var jsonResponse = performRequest(MockMvcRequestBuilders.post("/ubicaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(jsonResponse, clazz);
    }

    public <T> T recuperar(Long id, HttpStatus expectedStatus,  Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones/" + id))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public Collection<UbicacionDTO> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UbicacionDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UbicacionDTO.class)
        );

        return dtos;
    }

    public String eliminar(Long id) throws Throwable {
        return performRequest(MockMvcRequestBuilders.delete("/ubicaciones/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    public Collection<EspirituDTO> espiritusEn (Long id, HttpStatus expectedStatus) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones/" + id + "/espiritus"))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos;
    }

    public Collection<MediumDTO> mediumSinEspiritusEn (Long id, HttpStatus expectedStatus) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones/" + id + "/mediumSinEspiritus"))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        Collection<MediumDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
        );

        return dtos;
    }

    public <T> T santuarioCorrupto (HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones/reporteSantuarioMasCorrupto"))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value())).andDo(print())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

}