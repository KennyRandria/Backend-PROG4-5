package com.patrimoine.patrimoine.controller;

import com.patrimoine.patrimoine.model.Patrimoine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/patrimoines")
public class PatrimoineController {

    private final String FILE_PATH = "src/main/resources/patrimoines.txt";

    @PutMapping("/{id}")
    public ResponseEntity<Patrimoine> createOrUpdatePatrimoine(@PathVariable String id, @RequestBody Patrimoine patrimoine) {
        try {
            List<Patrimoine> patrimoines = readPatrimoinesFromFile();
            LocalDateTime now = LocalDateTime.now();
            patrimoine.setDerniereModification(now);

            // Vérifie si l'ID existe déjà
            boolean exists = patrimoines.stream().anyMatch(p -> p.getId().equals(id));

            if (!exists) {
                // Si l'ID n'existe pas, crée un nouveau Patrimoine
                patrimoine.setId(id); // On assigne l'ID depuis l'URL
                patrimoines.add(patrimoine);
            } else {
                // Si l'ID existe, mets à jour le Patrimoine
                for (int i = 0; i < patrimoines.size(); i++) {
                    if (patrimoines.get(i).getId().equals(id)) {
                        patrimoines.get(i).setPossesseur(patrimoine.getPossesseur());
                        patrimoines.get(i).setDerniereModification(now);
                        patrimoine.setId(id); // Assigner l'ID au patrimoine mis à jour
                        break;
                    }
                }
            }

            writePatrimoinesToFile(patrimoines);
            return ResponseEntity.ok(patrimoine); // Retourner le patrimoine avec l'ID
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Patrimoine> getPatrimoine(@PathVariable String id) {
        try {
            List<Patrimoine> patrimoines = readPatrimoinesFromFile();
            for (Patrimoine patrimoine : patrimoines) {
                if (patrimoine.getId().equals(id)) {
                    return ResponseEntity.ok(patrimoine);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    protected List<Patrimoine> readPatrimoinesFromFile() throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
            return lines.map(line -> {
                String[] parts = line.split(",");
                return new Patrimoine(parts[0], parts[1], LocalDateTime.parse(parts[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }).collect(Collectors.toList());
        }
    }

    private void writePatrimoinesToFile(List<Patrimoine> patrimoines) throws IOException {
        StringBuilder content = new StringBuilder();
        for (Patrimoine p : patrimoines) {
            content.append(p.getId()).append(",")
                    .append(p.getPossesseur()).append(",")
                    .append(p.getDerniereModification().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        }
        Files.write(Paths.get(FILE_PATH), content.toString().getBytes());
    }
}
