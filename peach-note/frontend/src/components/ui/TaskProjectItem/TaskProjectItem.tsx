import React from "react";
import style from "./TaskProjectItem.module.css";
import classNames from "classnames";
import TaskProjectItemProps from "./props/TaskProjectItemProps";
import { TiArrowSortedUp } from "react-icons/ti";
import { FaRegClock } from "react-icons/fa6";
import TaskService from "../../../services/TaskService";

export const TaskProjectItem: React.FC<TaskProjectItemProps> = ({
  taskId,
  title,
  statusName,
  deadline,
  openTaskWindow,
  triggerTableReload,
  setErrorMessage,
  openOrganizeTaskWindow,
  setSuccessMessage,
  triggerTaskListReload,
  handleMessageDisappearAnimation,
}) => {
  const handlePromoteStatus = async () => {
    try {
      if (statusName === "Done") {
        setErrorMessage("how are you doing it");
        handleMessageDisappearAnimation();
        return;
      }
      if (statusName === "Bucket") {
        openOrganizeTaskWindow(taskId, title, true);
        triggerTaskListReload((prev) => prev + 1);
        triggerTableReload((prev) => prev + 1);
        return;
      }
      await TaskService.promoteStatus(taskId);
      triggerTaskListReload((prev) => prev + 1);
      triggerTableReload((prev) => prev + 1);
      setSuccessMessage("status promoted");
      handleMessageDisappearAnimation();
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error ||
          errorMessage?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
    triggerTableReload((prev) => prev + 1);
  };
  return (
    <>
      <div
        className={classNames(style.container)}
        onClick={() => openTaskWindow(taskId)}
      >
        <div
          className={classNames(style.title_container, {
            [style.last_status]: statusName === "Done",
          })}
        >
          <p>{title}</p>
        </div>
        <div
          className={classNames(style.bottom_bar, {
            [style.last_status]: statusName === "Done",
          })}
        >
          <p className={classNames(style.deadline)}>
            {deadline ? (
              <>
                <FaRegClock />
                {deadline}
              </>
            ) : null}
          </p>
          <p className={classNames(style.status_name)}>{statusName}</p>
        </div>
        {statusName === "Done" ? null : (
          <button
            className={classNames(style.promote_button)}
            onClick={(e) => {
              e.stopPropagation();
              handlePromoteStatus();
            }}
          >
            <TiArrowSortedUp className={classNames(style.arrow)} />
          </button>
        )}
      </div>
    </>
  );
};
