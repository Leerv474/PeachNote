import React, { useState } from "react";
import style from "./Table.module.css";
import classNames from "classnames";
import TableProps from "./props/TableProps";
import { TaskItem } from "../TaskItem/TaskItem";

export const Table: React.FC<TableProps> = ({
  title,
  isLastStatus = false,
  openTaskWindow,
}) => {
  const taskList = [
    "super duper long ass string of characters that is needed to test this wrap capabilities",
    "test",
  ];
  return (
    <>
      <div className={classNames(style.table_container)}>
        <div className={classNames(style.table_title_container)}>{title}</div>
        <div className={classNames(style.task_list)}>
          {taskList.map((value) => (
            <TaskItem
              title={value}
              taskId={1}
              isLastStatus={isLastStatus}
              openTaskWindow={openTaskWindow}
            />
          ))}
        </div>
      </div>
    </>
  );
};
