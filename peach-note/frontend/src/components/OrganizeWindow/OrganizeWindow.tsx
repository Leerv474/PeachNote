import React, { useState } from "react";
import style from "./OrganizeWindow.module.css";
import classNames from "classnames";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import OrganizeWindowProps from "./props/OrganizeWindowProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { FaRegCalendar, FaRegClock } from "react-icons/fa";
import { SlActionRedo } from "react-icons/sl";
import { GoProjectRoadmap } from "react-icons/go";
import TaskService from "../../services/TaskService";

export const OrganizeWindow: React.FC<OrganizeWindowProps> = ({
  taskData,
  setShowOrganizeWindow,
  triggerTableReload,
  triggerProjectListReload,
  triggerTaskListReload,
}) => {
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [disappear, setDisappear] = useState(false);
  const handleMessageDisappearAnimation = () => {
    setTimeout(() => {
      setDisappear(true);
    }, 1000);
    setTimeout(() => {
      setErrorMessage("");
      setSuccessMessage("");
      setDisappear(false);
    }, 1300);
  };

  const convertToProject = 0;
  const putInAwait = 1;
  const putInDelayed = 2;
  const putInCurrent = 3;
  const handleConvert = async (actionNumber: number) => {
    if (!taskData?.taskId) {
      return;
    }
    const taskId = taskData.taskId;
    try {
      switch (actionNumber) {
        case 0:
          await TaskService.convertToProject(taskId);
          triggerProjectListReload((prev) => prev + 1);
          break;
        case 1:
          await TaskService.putInAwait(taskId);
          break;
        case 2:
          await TaskService.putInDelayed(taskId);
          break;
        case 3:
          await TaskService.putInCurrent(taskId);
          break;
        default:
          return;
      }
      setSuccessMessage("task status updated");
      handleMessageDisappearAnimation();
      triggerTableReload((prev) => prev + 1);
      if (taskData.isTaskProject) {
        triggerProjectListReload((prev) => prev + 1);
        triggerTaskListReload((prev) => prev + 1);
      }
      setShowOrganizeWindow(false);
    } catch (error: any) {
      const errorReponse = error.response;
      setErrorMessage(
        errorReponse?.error ||
          errorReponse?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
  };

  return (
    <>
      <ModelWindow
        setShowWindow={setShowOrganizeWindow}
        windowSizeCss={classNames(style.window_size)}
        successMessage={successMessage}
        errorMessage={errorMessage}
        disappear={disappear}
      >
        <div className={classNames(style.title_container)}>
          <p className={classNames(style.title)}>
            {taskData?.title || "supposedly title of"}
          </p>
        </div>
        <div className={classNames(style.options_container)}>
          {!taskData?.isTaskProject ? (
            <div className={classNames(style.option)}>
              <div className={classNames(style.option_context)}>
                <GoProjectRoadmap />
                <p>task has multiple steps</p>
              </div>
              <ActionButton
                classname={classNames(style.action_button)}
                label={"Convert to a project"}
                onClick={() => {
                  handleConvert(convertToProject);
                }}
              />
            </div>
          ) : null}
          <div className={classNames(style.option)}>
            <div className={classNames(style.option_context)}>
              <FaRegClock />
              <p>task depends on external factors</p>
            </div>
            <ActionButton
              classname={classNames(style.action_button)}
              label={"Put into Await"}
              onClick={() => {
                handleConvert(putInAwait);
              }}
            />
          </div>
          <div className={classNames(style.option)}>
            <div className={classNames(style.option_context)}>
              <FaRegCalendar />
              <p>task can be delayed</p>
            </div>
            <ActionButton
              classname={classNames(style.action_button)}
              label={"Put into Delayed"}
              onClick={() => {
                handleConvert(putInDelayed);
              }}
            />
          </div>
          <div className={classNames(style.option)}>
            <div className={classNames(style.option_context)}>
              <SlActionRedo />
              <p>task can be started now</p>
            </div>
            <ActionButton
              classname={classNames(style.action_button)}
              label={"Put into Current"}
              onClick={() => {
                handleConvert(putInCurrent);
              }}
            />
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
