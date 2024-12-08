import React from "react";
import style from "./ProjectWindow.module.css";
import classNames from "classnames";
import ProjectWindowProps from "./props/ProjectWindowProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";

export const ProjectWindow: React.FC<ProjectWindowProps> = ({
  projectId,
  setShowProject,
}) => {
  return (
    <>
      <ModelWindow
        setShowWindow={setShowProject}
      >
      <h1>{projectId}</h1>
      </ModelWindow>
    </>
  );
};
