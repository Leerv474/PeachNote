import React from "react";
import style from "./TaskWindow.module.css";
import classNames from "classnames";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import TaskWindowProps from "./props/TaskWindowProps";

export const TaskWindow: React.FC<TaskWindowProps> = ({
  taskId,
  setShowTaskWindow,
}) => {
  return (
    <>
      <ModelWindow
        setShowWindow={setShowTaskWindow}
        windowSizeCss={classNames(style.windowSizing)}
      >
        <h1>{taskId}</h1>
      </ModelWindow>
    </>
  );
};
