import React, { useEffect, useRef, useState } from "react";
import style from "./ProjectItem.module.css";
import classNames from "classnames";
import ProjectItemProps from "./props/ProjectItemProps";

export const ProjectItem: React.FC<ProjectItemProps> = ({
  projectId,
  title,
  tasksAmount,
  tasksCompleted,
  openProjectWindow,
}) => {
  const [progressWidth, setProgressWidth] = useState(0);

  useEffect(() => {
    setProgressWidth(tasksCompleted / tasksAmount);
  }, [tasksAmount, tasksCompleted]);

  const handleOnClick = () => {
    openProjectWindow(projectId)
  };
  return (
    <>
      <div className={classNames(style.item_container)} onClick={handleOnClick}>
        <p>{title}</p>
        <div className={classNames(style.progress_bar_container)}>
          <p>
            {tasksCompleted}/{tasksAmount}
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
