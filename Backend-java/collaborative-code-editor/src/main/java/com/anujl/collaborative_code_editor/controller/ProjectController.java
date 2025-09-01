package com.anujl.collaborative_code_editor.controller;

import com.anujl.collaborative_code_editor.dto.CodeRequest;
import com.anujl.collaborative_code_editor.dto.ProjectDTO;
import com.anujl.collaborative_code_editor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ProjectDTO p) {
        System.out.println("service called");
        return ResponseEntity.ok(projectService.save(p));
    }

    @GetMapping("/{id}")
    public ProjectDTO get(@PathVariable String id) {
        return projectService.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody ProjectDTO updated) {

        return ResponseEntity
                .ok(projectService.update(id,updated));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        projectService.deleteById(id);
    }

        @PostMapping("/execute")
        public ResponseEntity<String> executeCode(@RequestBody CodeRequest request) {
            String language = request.getLanguage().toLowerCase();
            String code = request.getCode();

            try {
                Path tempDir = Files.createTempDirectory("codeExec");
                Path tempFile;
                ProcessBuilder processBuilder;

                switch (language) {
                    case "java":
                        tempFile = tempDir.resolve("Main.java");
                        Files.write(tempFile, code.getBytes());


                        processBuilder = new ProcessBuilder(
                                "docker", "run", "--rm",
                                "-v", tempDir.toAbsolutePath() + ":/app",
                                "openjdk:17",
                                "bash", "-c", "javac /app/Main.java && java -cp /app Main"
                        );
                        break;

                    case "python":
                        tempFile = tempDir.resolve("script.py");
                        Files.write(tempFile, code.getBytes());


                        processBuilder = new ProcessBuilder(
                                "docker", "run", "--rm",
                                "-v", tempDir.toAbsolutePath() + ":/app",
                                "python:3.10",
                                "python3", "/app/script.py"
                        );
                        break;

                    case "c":
                        tempFile = tempDir.resolve("program.c");
                        Files.write(tempFile, code.getBytes());


                        processBuilder = new ProcessBuilder(
                                "docker", "run", "--rm",
                                "-v", tempDir.toAbsolutePath() + ":/app",
                                "gcc:latest",
                                "bash", "-c", "gcc /app/program.c -o /app/program && /app/program"
                        );
                        break;

                    case "cpp":
                    case "c++":
                        tempFile = tempDir.resolve("program.cpp");
                        Files.write(tempFile, code.getBytes());


                        processBuilder = new ProcessBuilder(
                                "docker", "run", "--rm",
                                "-v", tempDir.toAbsolutePath() + ":/app",
                                "gcc:latest",
                                "bash", "-c", "g++ /app/program.cpp -o /app/program && /app/program"
                        );
                        break;

                    default:
                        return ResponseEntity.badRequest().body("Unsupported language: " + language);
                }

                Process runProcess = processBuilder.start();
                String output = new String(runProcess.getInputStream().readAllBytes());
                String errorOutput = new String(runProcess.getErrorStream().readAllBytes());

                int exitCode = runProcess.waitFor();

                if (exitCode != 0) {
                    return ResponseEntity.badRequest().body("Execution Error:\n" + errorOutput);
                }

                return ResponseEntity.ok(output);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Execution failed: " + e.getMessage());
            }
        }


}
