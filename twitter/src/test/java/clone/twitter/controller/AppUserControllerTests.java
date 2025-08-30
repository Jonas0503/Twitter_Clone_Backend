package clone.twitter.controller;

import clone.twitter.TestFactory.AppUserTestFactory;
import clone.twitter.dto.AppUserDTO;
import clone.twitter.exceptions.EntityAlreadyExistsException;
import clone.twitter.exceptions.EntityNotFoundException;
import clone.twitter.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        AppUserDTO appUserDTO = AppUserTestFactory.createAppUserDTO();
        UUID id = appUserDTO.getId();

        Mockito.when(appUserService.readAppUser(id)).thenReturn(appUserDTO);

        // Act + Assert
        MvcResult mvcResult = mockMvc.perform(get("/user/" + id))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseJson = mvcResult.getResponse().getContentAsString();
        AppUserDTO appUserDTOResult = mapper.readValue(responseJson, AppUserDTO.class);

        assertEquals(appUserDTO, appUserDTOResult);
    }


    @Test
    public void testGetAppUserThrowsEntityNotFoundException() throws Exception {
        // Arrange
        UUID idNotExistentAppUser = UUID.randomUUID();
        Mockito.when(appUserService.readAppUser(idNotExistentAppUser)).thenThrow(new EntityNotFoundException(idNotExistentAppUser));

        // Act + Assert
        mockMvc.perform(get("/user/" + idNotExistentAppUser))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testCreateNewUser() throws Exception {
        // Arrange
        AppUserDTO appUserDTO = AppUserTestFactory.createAppUserDTO();
        Mockito.when(appUserService.createAppUser(appUserDTO)).thenReturn(appUserDTO);

        // Act + Assert
        MvcResult mvcResult = mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(appUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        String responseJson = mvcResult.getResponse().getContentAsString();
        AppUserDTO appUserDTOResult = mapper.readValue(responseJson, AppUserDTO.class);

        assertEquals(appUserDTO, appUserDTOResult);
    }


    @Test
    public void testCreateNewUserThrowsEntityAlreadyExistsException() throws Exception {
        // Arrange
        AppUserDTO appUserDTO = AppUserTestFactory.createAppUserDTO();
        Mockito.when(appUserService.createAppUser(appUserDTO)).thenThrow(new EntityAlreadyExistsException(appUserDTO.getId()));

        // Act + Assert
        mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(appUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateUser() throws Exception {
        // Arrange
        AppUserDTO appUserDTO = AppUserTestFactory.createAppUserDTO();
        Mockito.when(appUserService.updateAppUser(appUserDTO)).thenReturn(appUserDTO);

        // Act + Assert
        MvcResult mvcResult = mockMvc.perform(put("/user")
                        .content(mapper.writeValueAsString(appUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseJson = mvcResult.getResponse().getContentAsString();
        AppUserDTO appUserDTOResult = mapper.readValue(responseJson, AppUserDTO.class);

        assertEquals(appUserDTO, appUserDTOResult);
    }


    @Test
    public void testUpdateUserThrowsEntityNotFoundException() throws Exception {
        // Arrange
        AppUserDTO appUserDTO = AppUserTestFactory.createAppUserDTO();
        Mockito.when(appUserService.updateAppUser(appUserDTO)).thenThrow(new EntityNotFoundException(appUserDTO.getId()));

        // Act + Assert
        mockMvc.perform(put("/user")
                        .content(mapper.writeValueAsString(appUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleterUser() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(appUserService).deleteAppUser(id);

        // Act + Assert
        mockMvc.perform(delete("/user/" + id))
                .andExpect(status().isNoContent());
    }

    // TODO: Als nächstes den AppUserService testen
}
