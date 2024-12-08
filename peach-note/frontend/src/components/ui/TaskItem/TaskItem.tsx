import React from "react";
import style from "./TaskItem.module.css";
import classNames from "classnames";
import TaskItemProps from "./props/TaskItemProps";
import { TiArrowSortedUp } from "react-icons/ti";

export const TaskItem: React.FC<TaskItemProps> = ({
  title,
  taskId,
  isLastStatus,
  openTaskWindow,
}) => {
  return (
    <>
      <div className={classNames(style.task_item)}>
        <div
          className={classNames(style.button_container)}
          onClick={() => {
            openTaskWindow(taskId);
          }}
        >
          <p className={classNames({ [style.last_status]: isLastStatus })}>
            {title}
          </p>
        </div>
        {!isLastStatus ? (
          <div
            className={classNames(style.next_status_button)}
            onClick={(e) => {
              e.stopPropagation();
            }}
          >
            <TiArrowSortedUp className={classNames(style.arrow)} />
          </div>
        ) : null}
      </div>
    </>
  );
};
