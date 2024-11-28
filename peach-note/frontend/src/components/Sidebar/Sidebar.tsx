import React from "react";
import style from "./Sidebar.module.css";
import classNames from "classnames";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import SidebarProps from "./props/SidebarProps";
import { BoardItem } from "../ui/BoardItem/BoardItem";

export const Sidebar: React.FC<SidebarProps> = ({
  boardNameList,
  boardMap,
  sidebarOpen,
}) => {
  const handleCreateBoard = () => {};
  const testNames = [
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
    "test",
  ];
  return (
    <>
      <div
        className={classNames(style.sidebar, { [style.reveal]: sidebarOpen })}
      >
        <div
          className={classNames(style.board_list_bar, {
            [style.reveal]: sidebarOpen,
          })}
        >
          <p>boards</p>
        </div>
        <div className={classNames(style.content_container)}>
          <div className={style.board_list_container}>
            {testNames.map((name) => (
              <BoardItem name={name} boardId={1} />
            ))}
          </div>
          <div className={style.create_button_container}>
            <ActionButton
              classname={style.create_board_pos}
              onClick={handleCreateBoard}
              label="create"
            />
          </div>
        </div>
      </div>
    </>
  );
};
