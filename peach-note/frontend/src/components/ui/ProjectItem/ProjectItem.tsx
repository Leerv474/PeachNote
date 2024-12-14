import React, { useEffect, useState } from "react";
import style from "./ProjectItem.module.css";
import classNames from "classnames";
import ProjectItemProps from "./props/ProjectItemProps";

export const ProjectItem: React.FC<ProjectItemProps> = ({
  projectId,
  title,
  tasksAmount,
  finishedTasksAmount,
  openProjectWindow,
}) => {
  const [progressWidth, setProgressWidth] = useState(0);

  useEffect(() => {
    if (tasksAmount === 0) {
      setProgressWidth(0);
      return;
    }
    setProgressWidth(finishedTasksAmount / tasksAmount);
  }, [tasksAmount, finishedTasksAmount]);

  const handleOnClick = () => {
    openProjectWindow(projectId);
  };
  return (
    <>
      <div className={classNames(style.item_container)} onClick={handleOnClick}>
        <p className={classNames(style.title)}>{title}</p>
        <div className={classNames(style.progress_bar_container)}>
          <p>
            {finishedTasksAmount}/{tasksAmount}
          </p>
          <progress
            value={progressWidth}
            max={1}
            className={classNames(style.progress_bar)}
          />
        </div>
      </div>
    </>
  );
};
