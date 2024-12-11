import React, { useContext, useEffect, useState } from "react";
import style from "./Sidebar.module.css";
import classNames from "classnames";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import SidebarProps from "./props/SidebarProps";
import { BoardItem } from "../ui/BoardItem/BoardItem";
import { Context } from "../..";
import IBoardItem from "../../interfaces/IBoardItem";

export const Sidebar: React.FC<SidebarProps> = ({
  setShowCreateBoard,
  setBoardId,
  sidebarOpen,
  openBoardSettingsWindow,
}) => {
  const handleCreateBoard = () => {
    setShowCreateBoard(true);
  };

  const [boardList, setBoardList] = useState<Array<IBoardItem>>();
  const { store } = useContext(Context);

  useEffect(() => {
    const fetchBoards = async () => {
      const boards = await store.findAllBoards();
      setBoardList(boards);
    };
    fetchBoards();
  }, []);

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
            {boardList && boardList.length !== 0 ? 
              Object.entries(boardList).map(([value, { boardId, name }]) => {
                return (
                  <BoardItem
                    key={boardId}
                    name={name}
                    boardId={Number(boardId)}
                    setBoardId={setBoardId}
                    openBoardSettingsWindow={openBoardSettingsWindow}
                  ></BoardItem>
                );
              }): 
            <p className={classNames(style.empty_message)}>no boards created</p>}
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
