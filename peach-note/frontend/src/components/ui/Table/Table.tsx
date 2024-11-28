import React from "react";
import style from "./Table.module.css";
import classNames from "classnames";
import TableProps from "./props/TableProps";
import { TaskItem } from "../TaskItem/TaskItem";

export const Table: React.FC<TableProps> = ({ title }) => {
  const taskList = [
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
  ];
  return (
    <>
      <div className={classNames(style.table_container)}>
        <div className={classNames(style.table_title_container)}>{title}</div>
        <div className={classNames(style.task_list)}>
          {taskList.map((value) => (
            <TaskItem title={value} taskId={1} />
          ))}
        </div>
      </div>
    </>
  );
};
