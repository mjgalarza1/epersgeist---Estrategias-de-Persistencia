package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.modelo.Direccion;
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

@Component
public class MockMVCEspirituController {

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

    public  <T> T crear(CreateEspirituDTO createEspirituDTO, HttpStatus expectedStatus,  Class<T> clazz) throws Throwable {
        var json = objectMapper.writeValueAsString(createEspirituDTO);

        var jsonResponse = performRequest(MockMvcRequestBuilders.post("/espiritus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(jsonResponse, clazz);
    }

    public <T> T recuperar(Long espirituId, HttpStatus expectedStatus,  Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/espiritus/" + espirituId))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, clazz);
    }

    public Collection<EspirituDTO> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/espiritus"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos;
    }

    public String eliminar(Long espirituId) throws Throwable {
        return performRequest(MockMvcRequestBuilders.delete("/espiritus/" + espirituId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    public <T> T conectar(Long espirituId, Long mediumId, HttpStatus expectedStatus,  Class<T> clazz) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.put("/espiritus/" + espirituId + "/conectarA/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, clazz);
    }

    public int espiritusDemoniacos(Direccion dir, int pagina, int cantidadPorPagina, HttpStatus expectedStatus) throws Throwable {
        return performRequest(MockMvcRequestBuilders.get("/espiritus/demoniacos")
                .param("dir", String.valueOf(dir))
                .param("pagina", String.valueOf(pagina))
                .param("cantidadPorPagina", String.valueOf(cantidadPorPagina)))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getStatus();
    }
}