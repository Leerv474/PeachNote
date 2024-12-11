package io.leerv.peach_note.task;

import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.statusTable.StatusTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    private final TaskRepository repository;

    public void putTaskIntoBucket(Task task, StatusTable bucket) {
        task.setStatusTable(bucket);
        repository.save(task);
    }

    public void deleteAllTasks(List<Task> taskList) {
        repository.deleteAll(taskList);
    }

    public Task findTaskById(Long taskId) {
        return repository.findById(taskId)
                .orElseThrow(() -> new RecordNotFound("Task not found"));
    }

    public long calculatePriority(Task task) {
        if (task.getDeadline() == null) {
            return 0;
        }
        long daysUntilDeadline = ChronoUnit.DAYS.between(task.getCreationDate(), task.getDeadline());
        return daysUntilDeadline > 0 ? daysUntilDeadline : 0;
    }

    public boolean isUniqueTaskTitle(String title, Long boardId) {
        return repository.isUniqueTitleInBoard(title, boardId);
    }
}
