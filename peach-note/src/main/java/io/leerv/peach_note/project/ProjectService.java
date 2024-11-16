package io.leerv.peach_note.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;

    public void deleteAllProjects(List<Project> projectList) {
        repository.deleteAll(projectList);
    }
}
