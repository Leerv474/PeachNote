import React, { useState } from "react";
import style from "./ProjectList.module.css";
import classNames from "classnames";
import { TiArrowSortedUp } from "react-icons/ti";
import { ProjectItem } from "../ProjectItem/ProjectItem";
import ProjectListProps from "./props/ProjectListProps";

export const ProjectList: React.FC<ProjectListProps> = ({
  openProjectWindow,
}) => {
  const [listExpanded, setListExpanded] = useState(false);
  // TODO: replce with actual data
  const projects = [
    {
      title: "PROJECT",
      projectId: 1,
      tasksAmount: 20,
      tasksCompleted: 16,
    },
    {
      title: "PROJECT",
      projectId: 2,
      tasksAmount: 20,
      tasksCompleted: 1,
    },
    {
      title: "PROJECT",
      projectId: 3,
      tasksAmount: 20,
      tasksCompleted: 12,
    },
    {
      title: "PROJECT",
      projectId: 4,
      tasksAmount: 20,
      tasksCompleted: 17,
    },
    {
      title: "PROJECT",
      projectId: 1,
      tasksAmount: 20,
      tasksCompleted: 17,
    },
    {
      title: "PROJECT",
      projectId: 1,
      tasksAmount: 20,
      tasksCompleted: 17,
    },
  ];
  return (
    <>
      <div
        className={classNames(style.container, {
          container_expanded: listExpanded,
        })}
      >
        <div
          className={classNames(style.button_container, {
            [style.button_container_expanded]: listExpanded,
          })}
        >
          <div
            onClick={() => {
              setListExpanded(!listExpanded);
            }}
            className={classNames(style.expand_button, {
              [style.expand_button_expanded]: listExpanded,
            })}
          >
            <TiArrowSortedUp
              className={classNames(style.arrow, {
                [style.arrow_rotated]: listExpanded,
              })}
            />
            <p>Projects</p>
          </div>
        </div>
        <div
          className={classNames(style.list_container, {
            [style.list_expanded]: listExpanded,
          })}
        >
          <div className={classNames(style.scroll_container)}>
            {projects.map(
              ({ title, projectId, tasksAmount, tasksCompleted }) => {
                return (
                  <ProjectItem
                    key={projectId}
                    projectId={projectId}
                    title={title}
                    tasksAmount={tasksAmount}
                    tasksCompleted={tasksCompleted}
                    openProjectWindow={openProjectWindow}
                  />
                );
              },
            )}
          </div>
        </div>
      </div>
    </>
  );
};
