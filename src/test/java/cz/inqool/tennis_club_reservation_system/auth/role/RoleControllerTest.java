package cz.inqool.tennis_club_reservation_system.auth.role;

import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cz.inqool.tennis_club_reservation_system.TestUtils.convertToJson;
import static cz.inqool.tennis_club_reservation_system.auth.role.RoleFactory.createRoleCreateDto;
import static cz.inqool.tennis_club_reservation_system.auth.role.RoleFactory.createRoleDto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findAllRoles_withTwoRoles_shouldReturnPaginatedRoles() throws Exception {
        List<RoleDto> users = List.of(
                createRoleDto(1L, "USER"),
                createRoleDto(2L, "ADMIN")
        );

        when(roleService.findAllRoles(any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        mockMvc.perform(get("/api/v1/role/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("USER"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void newRole_withValidForm_shouldCreateAndReturnNewRole() throws Exception {
        var createDto = createRoleCreateDto("USER");
        var createdRoleDto = createRoleDto(1L, "USER");

        when(roleService.saveRole(createDto))
                .thenReturn(createdRoleDto);

        mockMvc.perform(put("/api/v1/role/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USER"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editRole_withValidForm_shouldEditAndReturnEditedRole() throws Exception {
        var roleDto = createRoleDto(1L, "USER");
        var editedRole = createRoleDto(1L, "USER");

        when(roleService.editRole(roleDto))
                .thenReturn(editedRole);

        mockMvc.perform(put("/api/v1/role/edit")
                        .content(convertToJson(roleDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USER"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editRole_withInvalidForm_returnsNotFound() throws Exception {
        var roleDto = createRoleDto(999L, "USER");

        when(roleService.editRole(roleDto))
                .thenThrow(new NotFoundException("Role with id 999 not found"));

        mockMvc.perform(put("/api/v1/role/edit")
                        .content(convertToJson(roleDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteRole_withInvalidId_returnsNotFound() throws Exception {
        when(roleService.deleteRole(999L))
                .thenThrow(new NotFoundException("Role with id 999 not found"));

        mockMvc.perform(delete("/api/v1/role/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteRole_withValidId_shouldDeleteUseAndReturnDeleted() throws Exception {
        var deleteDto = createRoleDto(1L, "USER");

        when(roleService.deleteRole(1L))
                .thenReturn(deleteDto);

        mockMvc.perform(delete("/api/v1/role/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USER"));
    }
}
