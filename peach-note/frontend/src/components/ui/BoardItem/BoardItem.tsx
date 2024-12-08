import React from "react";
import style from "./BoardItem.module.css";
import classNames from "classnames";
import { RiSettings3Fill } from "react-icons/ri";
import BoardItemProps from "./props/BoardItemProps";

export const BoardItem: React.FC<BoardItemProps> = ({
  name,
  boardId,
  setBoardId,
  openBoardSettingsWindow
}) => {
  //TODO: pass useState through props
  return (
    <>
      <div
        className={classNames(style.board_item)}
        onClick={() => {
          setBoardId(boardId);
        }}
      >
        <p>{name}</p>
        <RiSettings3Fill
          onClick={(e) => {
            e.stopPropagation();
            openBoardSettingsWindow(boardId);
          }}
          className={classNames(style.board_options_button)}
        />
      </div>
    </>
  );
};
