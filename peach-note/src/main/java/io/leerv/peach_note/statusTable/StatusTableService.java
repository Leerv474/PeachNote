package io.leerv.peach_note.statusTable;

import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.permission.BoardPermissionUtil;
import io.leerv.peach_note.statusTable.dto.StatusTableFullViewResponse;
import io.leerv.peach_note.statusTable.dto.StatusTableSimpleViewResponse;
import io.leerv.peach_note.task.TaskMapper;
import io.leerv.peach_note.task.dto.ExpandedTaskDto;
import io.leerv.peach_note.task.dto.SimpleTaskDto;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusTableService {
    private final StatusTableRepository repository;
    private final BoardPermissionUtil boardPermissionService;
    private final TaskMapper taskMapper;


    public void rename(User user, Long tableId, String name) {
        StatusTable statusTable = repository.findById(tableId)
                .orElseThrow(() -> new RecordNotFound("Status table not found"));

        if (!boardPermissionService.userIsCreator(user.getId(), statusTable.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to edit this status table");
        }
        statusTable.setName(name);
        repository.save(statusTable);
    }

    public StatusTableSimpleViewResponse simpleView(User user, Long tableId) {
        StatusTable statusTable = repository.findById(tableId)
                .orElseThrow(() -> new RecordNotFound("Status table not found"));
        if (!boardPermissionService.userHasAccess(user.getId(), statusTable.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to view this status table");
        }
        List<SimpleTaskDto> simpleViewtaskList = statusTable.getTaskList().stream().map(taskMapper::mapToSimpleView).toList();
        return StatusTableSimpleViewResponse.builder()
                .tableId(tableId)
                .taskList(simpleViewtaskList)
                .build();
    }

    public StatusTableFullViewResponse fullView(User user, Long tableId) {
        StatusTable statusTable = repository.findById(tableId)
                .orElseThrow(() -> new RecordNotFound("Status table not found"));
        if (!boardPermissionService.userHasAccess(user.getId(), statusTable.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to view this status table");
        }
        List<ExpandedTaskDto> expandedTaskList = statusTable.getTaskList().stream().map(taskMapper::mapToExpandedView).toList();
        return StatusTableFullViewResponse.builder()
                .tableId(tableId)
                .taskList(expandedTaskList)
                .build();
    }
}