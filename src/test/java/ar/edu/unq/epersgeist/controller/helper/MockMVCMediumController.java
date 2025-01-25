package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.ControllerTestConfiguration;
import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
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

@Component
public class MockMVCMediumController {

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


    public <T> T crear(CreateMediumDTO createDTO, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = objectMapper.writeValueAsString(createDTO);

        var jsonResponse = performRequest(MockMvcRequestBuilders.post("/mediums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(jsonResponse, clazz);
    }

    public <T> T recuperar(Long id, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/mediums/" + id))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public Collection<MediumDTO> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/mediums"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
        );

    }

    public String eliminar(Long id) throws Throwable {
        return performRequest(MockMvcRequestBuilders.delete("/mediums/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    public Collection<EspirituDTO> espiritus(Long id) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/mediums/" + id + "/espiritus"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );
    }

    public <T> T invocar(Long idMedium, Long idEspiritu, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/mediums/" + idMedium + "/invocarA/" + idEspiritu))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public <T> T mover(Long idMedium, Double latitud, Double longitud, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.put("/mediums/" + idMedium + "/moverA/" + latitud + "/" + longitud))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public <T> T descansar(Long idMedium, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.put("/mediums/" + idMedium + "/descansar"))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public <T> T exorcizar(Long id, Long idAExorcizar, HttpStatus expectedStatus, Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.put("/mediums/" + id + "/exorcizarA/" + idAExorcizar))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }
}