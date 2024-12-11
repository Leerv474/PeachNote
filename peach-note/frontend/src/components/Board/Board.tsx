import React, { useContext, useEffect, useRef, useState } from "react";
import style from "./Board.module.css";
import classNames from "classnames";
import BoardProps from "./props/BoardProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { Table } from "../ui/Table/Table";
import { ProjectList } from "../ui/ProjectList/ProjectList";
import { Context } from "../..";
import IBoard from "../../interfaces/IBoard";

export const Board: React.FC<BoardProps> = ({
  sidebarOpen,
  setShowCreateTask,
  openProjectWindow,
  openTaskWindow,
  boardData,
  tableReload,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  const { store } = useContext(Context);

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
                      isLastStatus={name === "Done"}
                      tableReload={tableReload}
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
                    setShowCreateTask(true);
                  }}
                />
              </div>
              <ProjectList
                boardId={boardData?.boardId || 0}
                openProjectWindow={openProjectWindow}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
