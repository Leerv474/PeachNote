import React from "react";
import style from "./TaskItem.module.css";
import classNames from "classnames";
import TaskItemProps from "./props/TaskItemProps";
import { TiArrowSortedUp } from "react-icons/ti";
import TaskService from "../../../services/TaskService";

export const TaskItem: React.FC<TaskItemProps> = ({
  title,
  taskId,
  isLastStatus,
  openOrganizeWindow,
  openTaskWindow,
  triggerTableReload,
  isInBucket,
}) => {
  const handleActionButton = async () => {
    if (isInBucket) {
      openOrganizeWindow(taskId, title);
      return;
    }
    try {
      await TaskService.promoteStatus(taskId);
      triggerTableReload((prev) => prev + 1);
    } catch (error: any) {
      console.log(error.reponse);
    }
  };

  return (
    <>
      <div className={classNames(style.task_item)}>
        <div
          className={classNames(style.title_container)}
          onClick={() => {
            openTaskWindow(taskId);
          }}
        >
          <p
            className={classNames(style.title, {
              [style.last_status]: isLastStatus,
            })}
          >
            {title}
          </p>
        </div>
        {!isLastStatus ? (
          <div
            className={classNames(style.next_status_button)}
            onClick={(e) => {
              e.stopPropagation();
              handleActionButton();
            }}
          >
            <TiArrowSortedUp className={classNames(style.arrow)} />
          </div>
        ) : null}
      </div>
    </>
  );
};
