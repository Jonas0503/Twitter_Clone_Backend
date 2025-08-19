package clone.twitter.controller;

import clone.twitter.dto.AppUserDTO;
import clone.twitter.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
public class AppUserControllerTests {

    @Autowired
    private AppUserController appUserController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserService appUserService;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
    }


    @Test
    public void testGetAppUser() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(id);
        appUserDTO.setUsername("jonas");

        Mockito.when(appUserService.readAppUser(id)).thenReturn(appUserDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/user/" + id))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseJson = mvcResult.getResponse().getContentAsString();
        AppUserDTO appUserDTOResult = mapper.readValue(responseJson, AppUserDTO.class);

        assertEquals(appUserDTO, appUserDTOResult);
    }
}
