import React, { useEffect, useRef } from "react";
import style from "./Board.module.css";
import classNames from "classnames";
import BoardProps from "./props/BoardProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { Table } from "../ui/Table/Table";
import { ProjectList } from "../ui/ProjectList/ProjectList";

export const Board: React.FC<BoardProps> = ({
  sidebarOpen,
  openCreateTaskWindow,
  openProjectWindow,
  openOrganizeWindow,
  openTaskWindow,
  boardData,
  tableReload,
  projectListReload,
  triggerTableReload,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
  }, [boardData])

  return (
    <>
      <div
        className={classNames(style.board_container, {
          [style.squeeze]: sidebarOpen,
        })}
      >
        <div className={classNames(style.relative_box)}>
          <div className={classNames(style.tables_container)}>
            <div className={classNames(style.scroll_container)} ref={scrollRef}>
              {boardData !== null ? (
                boardData?.statusTableList.map(({ tableId, name }) => {
                  return (
                    <Table
                      key={tableId}
                      tableId={tableId}
                      openTaskWindow={openTaskWindow}
                      isFirstStatus={name === "Bucket"}
                      isLastStatus={name === "Done"}
                      tableReload={tableReload}
                      openOrganizeWindow={openOrganizeWindow}
                      triggerTableReload={triggerTableReload}
                    />
                  );
                })
              ) : (
                <p className={classNames(style.no_board_message)}>
                  no board chosen
                </p>
              )}
            </div>
          </div>
          <div className={classNames(style.bottom_bar)}>
            <div className={classNames(style.bottom_container)}>
              <div className={classNames(style.create_task_container)}>
                <ActionButton
                  classname={classNames(style.create_task_button)}
                  label="new task"
                  onClick={() => {
                    if (boardData === null) {
                      return;
                    }
                    openCreateTaskWindow(-1, "");
                  }}
                />
              </div>
              <ProjectList
                boardId={boardData?.boardId || 0}
                openProjectWindow={openProjectWindow}
                projectListReload={projectListReload}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
