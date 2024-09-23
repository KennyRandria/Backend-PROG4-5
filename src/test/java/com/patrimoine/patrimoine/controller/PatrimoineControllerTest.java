package com.patrimoine.patrimoine.controller;
import com.patrimoine.patrimoine.model.Patrimoine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatrimoineControllerTest {

    private PatrimoineController controller;
    private final String FILE_PATH = "src/main/resources/patrimoines.txt";

    @BeforeEach
    void setUp() {
        controller = Mockito.spy(new PatrimoineController());
    }

    @Test
    void testCreateOrUpdatePatrimoine_New() throws IOException {
        Patrimoine patrimoine = new Patrimoine("1", "Jean Dupon", LocalDateTime.now());

        ResponseEntity<Patrimoine> response = controller.createOrUpdatePatrimoine("1", patrimoine);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jean Dupon", response.getBody().getPossesseur());
    }

    @Test
    void testCreateOrUpdatePatrimoine_Update() throws IOException {
        // Create the original patrimoine
        Patrimoine patrimoine = new Patrimoine("1", "Jean Dupon", LocalDateTime.now());
        controller.createOrUpdatePatrimoine("1", patrimoine);

        // Update patrimoine
        Patrimoine updatedPatrimoine = new Patrimoine("1", "Jean Dupont", LocalDateTime.now());
        ResponseEntity<Patrimoine> response = controller.createOrUpdatePatrimoine("1", updatedPatrimoine);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jean Dupont", response.getBody().getPossesseur());
    }

    @Test
    void testGetPatrimoine_Exists() throws IOException {
        // Mock the file writing and reading
        Files.write(Paths.get(FILE_PATH), ("1,Jean Dupon," + LocalDateTime.now() + "\n").getBytes());

        ResponseEntity<Patrimoine> response = controller.getPatrimoine("1");
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jean Dupon", response.getBody().getPossesseur());
    }

    @Test
    void testGetPatrimoine_NotFound() throws IOException {
        ResponseEntity<Patrimoine> response = controller.getPatrimoine("nonexistent");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetPatrimoine_IOError() throws IOException {
        // Simulate an IOException when reading the file
        Mockito.doThrow(new IOException()).when(controller).readPatrimoinesFromFile();

        ResponseEntity<Patrimoine> response = controller.getPatrimoine("1");
        assertEquals(500, response.getStatusCodeValue());
    }
}
