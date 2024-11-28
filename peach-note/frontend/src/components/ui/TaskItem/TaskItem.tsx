import React from "react";
import style from "./TaskItem.module.css";
import classNames from "classnames";
import TaskItemProps from "./props/TaskItemProps";

export const TaskItem: React.FC<TaskItemProps> = ({ title, taskId }) => {
  return (
    <>
      <div className={classNames(style.task_item)}>
        <p>{title}</p>
        <div className={classNames(style.next_status_button)}>&gt;</div>
      </div>
    </>
  );
};
