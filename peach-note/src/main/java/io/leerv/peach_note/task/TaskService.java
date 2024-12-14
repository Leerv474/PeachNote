package io.leerv.peach_note.task;

import io.leerv.peach_note.board.BoardUtil;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.permission.BoardPermissionUtil;
import io.leerv.peach_note.project.Project;
import io.leerv.peach_note.project.ProjectUtil;
import io.leerv.peach_note.statusTable.StatusTable;
import io.leerv.peach_note.statusTable.StatusTableUtil;
import io.leerv.peach_note.task.dto.*;
import io.leerv.peach_note.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    private final StatusTableUtil statusTableUtil;
    private final BoardPermissionUtil boardPermissionUtil;
    private final TaskUtil taskUtil;
    private final ProjectUtil projectUtil;
    private final BoardUtil boardUtil;


    @Transactional
    public TaskCreateResponse create(TaskCreateRequest request, User user) {
        boolean isEditor = boardPermissionUtil.userIsEditor(user.getId(), request.getBoardId());
        if (!isEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        if (taskUtil.isUniqueTaskTitle(request.getTitle(), request.getBoardId())) {
            throw new IllegalRequestContentException("Task must be unique in current board");
        }
        StatusTable statusTable = statusTableUtil.findBucketByBoardId(request.getBoardId());
        Project project = null;
        LocalDate deadline = null;
        if (request.getProjectId() != null) {
            project = projectUtil.findById(request.getProjectId());
        }
        if (request.getDeadline() != null) {
            deadline = request.getDeadline();
        }

        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creationDate(LocalDate.now())
                .deadline(deadline)
                .project(project)
                .statusTable(statusTable)
                .build();
        repository.save(newTask);

        return TaskCreateResponse.builder()
                .taskId(newTask.getId())
                .title(newTask.getTitle())
                .build();
    }

    public TaskDto find(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userHasRights = boardPermissionUtil.userHasAccess(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userHasRights) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .creationDate(task.getCreationDate())
                .completionDate(task.getCompletionDate())
                .projectId(task.getProject() == null ? null : task.getProject().getId())
                .statusTableId(task.getStatusTable().getId())
                .priority(taskUtil.calculatePriority(task))
                .build();
    }

    @Transactional
    public void edit(User user, TaskEditRequest request) {
        Task task = taskUtil.findTaskById(request.getTaskId());
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }

        if (request.getTitle() != null) {
            if (taskUtil.isUniqueTaskTitle(request.getTitle(), task.getStatusTable().getBoard().getId())) {
                throw new IllegalRequestContentException("Task must be unique in current board");
            }
            task.setTitle(request.getTitle());
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        repository.save(task);
    }

    @Transactional
    public void delete(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        repository.delete(task);
    }

    @Transactional
    public void promoteStatus(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        Integer currentDisplayOrder = task.getStatusTable().getDisplayOrder();
        Long boardId = task.getStatusTable().getBoard().getId();
        switch (currentDisplayOrder) {
            case 0:
                throw new IllegalRequestContentException("You can't promote a non organized task");
            case 1:
                task.setStatusTable(statusTableUtil.findCurrentByBoardId(boardId));
                break;
            case 2, 3:
                task.setStatusTable(statusTableUtil.findFirstCompletionStatus(boardId));
                break;
            default:
                task.setStatusTable(statusTableUtil.findNextStatus(currentDisplayOrder, boardId));
                break;
        }
        repository.save(task);
    }

    @Transactional
    public void changeStatus(User user, Long taskId, Long statusTableId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        if (!boardUtil.containStatus(task.getStatusTable().getBoard(), statusTableId)) {
            throw new IllegalRequestContentException("Status table isn't in current board's scope");
        }
        StatusTable newStatusTable = statusTableUtil.findTableById(statusTableId);
        StatusTable currentStatusTable = task.getStatusTable();
        if (newStatusTable.getId().equals(currentStatusTable.getId())) {
            throw new IllegalRequestContentException("Attempt to change to the same status");
        }
        task.setStatusTable(newStatusTable);
        repository.save(task);
    }

    public void convertToProject(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        projectUtil.createFromTask(task);
        repository.delete(task);
    }

    public void putInAwait(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        StatusTable awaitTable = statusTableUtil.findAwaitByBoardId(task.getStatusTable().getBoard().getId());
        task.setStatusTable(awaitTable);
        repository.save(task);
    }


    public void putInDelayed(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        StatusTable delayedTable = statusTableUtil.findDelayedByBoardId(task.getStatusTable().getBoard().getId());
        task.setStatusTable(delayedTable);
        repository.save(task);
    }

    public void putInCurrent(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsEditor = boardPermissionUtil.userIsEditor(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        StatusTable currentTable = statusTableUtil.findCurrentByBoardId(task.getStatusTable().getBoard().getId());
        task.setStatusTable(currentTable);
        repository.save(task);
    }

    public TaskDataResponse viewData(User user, Long taskId) {
        Task task = taskUtil.findTaskById(taskId);
        boolean userIsViewer = boardPermissionUtil.userHasAccess(user.getId(), task.getStatusTable().getBoard().getId());
        if (!userIsViewer) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }
        return TaskDataResponse.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectTitle(task.getProject() != null ? task.getProject().getTitle() : null)
                .userPermissionLevel(boardPermissionUtil.getUserPermission(task.getStatusTable().getBoard().getId(), user.getId()))
                .build();
    }
}