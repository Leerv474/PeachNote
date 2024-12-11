import React, { useContext, useEffect, useState } from "react";
import style from "./ProjectList.module.css";
import classNames from "classnames";
import { TiArrowSortedUp } from "react-icons/ti";
import { ProjectItem } from "../ProjectItem/ProjectItem";
import ProjectListProps from "./props/ProjectListProps";
import IProjectItem from "../../../interfaces/IProjectItem";
import { Context } from "../../..";

export const ProjectList: React.FC<ProjectListProps> = ({
  boardId,
  openProjectWindow,
}) => {
  const [listExpanded, setListExpanded] = useState(false);

  const { store } = useContext(Context);
  const [projectList, setProjectList] = useState<Array<IProjectItem>>();

  useEffect(() => {
    const fetchData = async () => {
      const data = await store.listProjects(boardId);
      setProjectList(data);
    };
    if (boardId < 0) {
      fetchData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // TODO: replce with actual data
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
            {projectList && projectList.length !== 0 ? (
              projectList.map(
                ({ title, projectId, tasksAmount, finishedTasksAmount }) => {
                  return (
                    <ProjectItem
                      key={projectId}
                      projectId={projectId}
                      title={title}
                      tasksAmount={tasksAmount}
                      finishedTasksAmount={finishedTasksAmount}
                      openProjectWindow={openProjectWindow}
                    />
                  );
                },
              )
            ) : (
              <div className={classNames(style.empty_message)}>
                <p>No project found</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};
