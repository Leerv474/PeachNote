import React, { useState } from "react";
import style from "./Dashboard.module.css";
import classNames from "classnames";
import { Navbar } from "../../components/Navbar/Navbar";
import { Sidebar } from "../../components/Sidebar/Sidebar";
import { Board } from "../../components/Board/Board";

export const Dashboard: React.FC = () => {
  //TODO: according to responses
  const boardTitle = "test";
  const username = "test";
  const boardNameList: Array<string> = [];
  const boardMap: Record<string, number> = {};

  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [showCreateBoard, setCreateBoard] = useState(false);

  return (
    <>
      <div className={classNames(style.dashboard_container)}>
        <Navbar
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
          boardTitle={boardTitle}
          username={username}
        />
        <Sidebar
          sidebarOpen={sidebarOpen}
          setCreateBoard={setCreateBoard}
          boardNameList={boardNameList}
          boardMap={boardMap}
        />
        <Board sidebarOpen={sidebarOpen}/>
      </div>
    </>
  );
};
