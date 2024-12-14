import React, { useContext, useEffect, useState } from "react";
import style from "./Table.module.css";
import classNames from "classnames";
import TableProps from "./props/TableProps";
import { TaskItem } from "../TaskItem/TaskItem";
import { Context } from "../../..";
import ITable from "../../../interfaces/ITable";

export const Table: React.FC<TableProps> = ({
  tableId,
  isFirstStatus = false,
  isLastStatus = false,
  openOrganizeWindow,
  openTaskWindow,
  tableReload,
  triggerTableReload,
}) => {
  const { store } = useContext(Context);

  const [tableData, setTableData] = useState<ITable>();
  useEffect(() => {
    const fetchData = async () => {
      const responseData = await store.viewTable(tableId);
      setTableData(responseData);
    };
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [tableReload]);

  return (
    <>
      <div className={classNames(style.table_container)}>
        <div className={classNames(style.table_title_container)}>
          {tableData?.name}
        </div>
        <div className={classNames(style.task_list)}>
          {tableData?.taskList.length !== 0 ? (
            tableData?.taskList
              .sort((a, b) => b.priority - a.priority)
              .map(({ taskId, title, priority }) => {
                return (
                  <TaskItem
                    key={taskId}
                    title={title}
                    taskId={taskId}
                    isInBucket={isFirstStatus}
                    isLastStatus={isLastStatus}
                    openTaskWindow={openTaskWindow}
                    openOrganizeWindow={openOrganizeWindow}
                    triggerTableReload={triggerTableReload}
                  />
                );
              })
          ) : (
            <div className={classNames(style.empty_message)}>
              <p>Empty table</p>
            </div>
          )}
        </div>
      </div>
    </>
  );
};
