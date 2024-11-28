import React from "react";
import style from "./Board.module.css";
import classNames from "classnames";
import BoardProps from "./props/BoardProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { Table } from "../ui/Table/Table";
import { ProjectList } from "../ui/ProjectList/ProjectList";

export const Board: React.FC<BoardProps> = ({ sidebarOpen }) => {
  return (
    <>
      <div
        className={classNames(style.board_container, {
          [style.squeeze]: sidebarOpen,
        })}
      >
        <div className={classNames(style.relative_box)}>
          <div className={classNames(style.tables_container)}>
            <div className={classNames(style.scroll_container)}>
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
              <Table title={"Done"} />
            </div>
          </div>
          <div className={classNames(style.bottom_bar)}>
            <div className={classNames(style.bottom_container)}>
              <div className={classNames(style.create_task_container)}>
                <ActionButton classname={classNames(style.create_task_button)}label="new task" onClick={() => {}} />
              </div>
              <ProjectList />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
