package io.leerv.peach_note.statusTable;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.task.TaskUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class StatusTableUtil {
    private final StatusTableRepository repository;
    private final TaskUtil taskUtil;

    public void createStatusTables(Board board, Map<String, Integer> additionalStatusTables) {
        List<String> defaultStatusNameList = List.of("Bucket", "Delayed", "Current", "Await");
        List<StatusTable> statusTableList = new ArrayList<>(IntStream.range(0, defaultStatusNameList.size())
                .mapToObj(i -> StatusTable.builder()
                        .name(defaultStatusNameList.get(i))
                        .displayOrder(i)
                        .board(board)
                        .taskList(new ArrayList<>())
                        .build()
                ).toList());

        if (additionalStatusTables != null) {
            statusTableList.addAll(
                    additionalStatusTables.entrySet()
                            .stream()
                            .map(entry -> StatusTable.builder()
                                    .name(entry.getKey())
                                    .displayOrder(entry.getValue())
                                    .board(board)
                                    .taskList(new ArrayList<>())
                                    .build()).toList()
            );
        }
        statusTableList.add(
                StatusTable.builder()
                        .name("Done")
                        .displayOrder(statusTableList.size())
                        .board(board)
                        .taskList(new ArrayList<>())
                        .build()
        );
        repository.saveAll(statusTableList);
    }

    @Transactional
    public void addStatusTables(Board board, Map<String, Integer> statusTableMap) {
        StatusTable doneTable = board.getStatusTableList().stream().filter(table -> table.getName().equals("Done")).findFirst()
                .orElseThrow(() -> new RecordNotFound("Board does not have Done table, which isn't possible"));
        int displayOrderLast = doneTable.getDisplayOrder();

        List<StatusTable> newStatusTables = statusTableMap.entrySet().stream()
                .map(entry ->
                        StatusTable.builder()
                                .board(board)
                                .name(entry.getKey())
                                .displayOrder(entry.getValue())
                                .taskList(new ArrayList<>())
                                .build()
                ).toList();

        repository.saveAll(newStatusTables);

        doneTable.setDisplayOrder(displayOrderLast + newStatusTables.size());
        repository.save(doneTable);
    }

    @Transactional
    public void removeStatusTables(Long boardId, List<Long> tablesToRemove) {
        List<StatusTable> statusTableList = repository.findAllById(tablesToRemove);
        StatusTable bucket = repository.findBucketByBoardId(boardId)
                .orElseThrow(() -> new RecordNotFound("Bucket table not found, which is not possible"));

        for (StatusTable st : statusTableList) {
            st.getTaskList().forEach(
                    task -> taskUtil.putTaskIntoBucket(task, bucket)
            );
            st.setTaskList(null);
        }
        repository.deleteAll(statusTableList);
    }

    @Transactional
    public void updateStatusTablesOrder(Long boardId, Map<Long, Integer> statusTableMap) {
        List<StatusTable> statusTableList = repository.findAllById(
                statusTableMap.keySet()
        );
        statusTableList.forEach(table -> table.setDisplayOrder(statusTableMap.get(table.getId())));
        repository.saveAll(statusTableList);
    }

    public void deleteAllTables(List<StatusTable> statusTableList) {
        statusTableList.forEach(statusTable -> taskUtil.deleteAllTasks(statusTable.getTaskList()));
        repository.deleteAll(statusTableList);
    }

    public StatusTable findBucketByBoardId(Long boardId) {
        return repository.findBucketByBoardId(boardId)
                .orElseThrow(() -> new RecordNotFound("Status table Bucket not found"));
    }

    public StatusTable findNextStatus(Integer currentDisplayOrder, Long boardId) {
        return repository.findByDisplayOrderAndBoardId(currentDisplayOrder + 1, boardId)
                .orElseThrow(() -> new RecordNotFound("Status table not found"));
    }

    public StatusTable findTableById(Long statusTableId) {
        return repository.findById(statusTableId)
                .orElseThrow(() -> new RecordNotFound("Status table not found"));
    }
}
