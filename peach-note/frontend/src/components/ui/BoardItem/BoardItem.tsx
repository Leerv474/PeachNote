import React from "react";
import style from "./BoardItem.module.css";
import classNames from "classnames";
import { RiSettings3Fill } from "react-icons/ri";
import BoardItemProps from "./props/BoardItemProps";

export const BoardItem: React.FC<BoardItemProps> = ({ name, boardId }) => {
  //TODO: pass useState through props
  const setBoard = (id: number) => {};
  const setOpenBoardOptions = (open: boolean) => {};
  return (
    <>
      <div className={classNames(style.board_item)}>
        <div
          className={classNames(style.board_button)}
          onClick={() => {
            setBoard(boardId);
          }}
        >
          {name}
        </div>
        <RiSettings3Fill
          onClick={() => setOpenBoardOptions(true)}
          className={classNames(style.board_options_button)}
        />
      </div>
    </>
  );
};
