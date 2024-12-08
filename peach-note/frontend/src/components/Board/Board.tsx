import React, { useRef } from "react";
import style from "./Board.module.css";
import classNames from "classnames";
import BoardProps from "./props/BoardProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { Table } from "../ui/Table/Table";
import { ProjectList } from "../ui/ProjectList/ProjectList";

export const Board: React.FC<BoardProps> = ({
  sidebarOpen,
  setShowCreateTask,
  openProjectWindow,
  openTaskWindow,
  boardId,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  const handleWheel = (e: React.WheelEvent) => {
    if (!scrollRef.current) return;
    e.preventDefault();
    scrollRef.current.scrollLeft += e.deltaY;
  };

  return (
    <>
      <div
        className={classNames(style.board_container, {
          [style.squeeze]: sidebarOpen,
        })}
      >
        <div className={classNames(style.relative_box)}>
          <div className={classNames(style.tables_container)}>
            <div
              className={classNames(style.scroll_container)}
              ref={scrollRef}
              onWheel={handleWheel}
            >
              <Table title={"Done"} openTaskWindow={openTaskWindow}/>
              <Table title={"Done"} openTaskWindow={openTaskWindow}/>
              <Table title={"Done"} openTaskWindow={openTaskWindow}/>
              <Table title={"Done"} openTaskWindow={openTaskWindow}/>
            </div>
          </div>
          <div className={classNames(style.bottom_bar)}>
            <div className={classNames(style.bottom_container)}>
              <div className={classNames(style.create_task_container)}>
                <ActionButton
                  classname={classNames(style.create_task_button)}
                  label="new task"
                  onClick={() => {
                    setShowCreateTask(true);
                  }}
                />
              </div>
              <ProjectList openProjectWindow={openProjectWindow}/>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
