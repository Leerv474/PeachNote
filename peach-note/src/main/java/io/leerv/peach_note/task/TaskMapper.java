package io.leerv.peach_note.task;

import io.leerv.peach_note.statusTable.StatusTableMapper;
import io.leerv.peach_note.task.dto.ExpandedTaskDto;
import io.leerv.peach_note.task.dto.SimpleTaskDto;
import io.leerv.peach_note.task.dto.TaskItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final TaskUtil taskUtil;
    private final StatusTableMapper statusTableMapper;

    public SimpleTaskDto mapToSimpleView(Task task) {
        return SimpleTaskDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .priority(taskUtil.calculatePriority(task))
                .isTaskProject(task.getProject() != null)
                .build();
    }

    public ExpandedTaskDto mapToExpandedView(Task task) {
        return ExpandedTaskDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .deadline(task.getDeadline())
                .priority(taskUtil.calculatePriority(task))
                .projectId(task.getProject().getId())
                .projectTitle(task.getProject().getTitle())
                .build();
    }

    public TaskItemDto mapToTaskItemDto(Task task) {
        return TaskItemDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .priority(taskUtil.calculatePriority(task))
                .deadline(task.getDeadline())
                .statusTable(statusTableMapper.mapToStatusTableItemDto(task.getStatusTable()))
                .build();
    }
}
