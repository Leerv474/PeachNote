import React, { useState } from "react";
import style from "./Dashboard.module.css";
import classNames from "classnames";
import { Navbar } from "../../components/Navbar/Navbar";
import { Sidebar } from "../../components/Sidebar/Sidebar";
import { Board } from "../../components/Board/Board";
import { CreateBoardWindow } from "../../components/CreateBoardWindow/CreateBoardWindow";
import { CreateTaskWindow } from "../../components/CreateTaskWindow/CreateTaskWindow";
import { ProjectWindow } from "../../components/ProjectWindow/ProjectWindow";
import { TaskWindow } from "../../components/TaskWindow/TaskWindow";
import { BoardSettingsWindow } from "../../components/BoardSettingsWindow/BoardSettingsWindow";

export const Dashboard: React.FC = () => {
  //TODO: according to responses
  const boardTitle = "test";
  const username = "test";
  const boardNameList: Array<string> = [];
  const boardMap: Record<string, number> = {};

  const [boardId, setBoardId] = useState(-1);

  const [sidebarOpen, setSidebarOpen] = useState(false);
  // windows
  const [showCreateBoardWindow, setShowCreateBoard] = useState(false);
  const [showCreateTaskWindow, setShowCreateTask] = useState(false);

  const [showProjectWindow, setShowProject] = useState(false);
  const [projectId, setProjectId] = useState(-1);
  const openProjectWindow = (id: number) => {
    setProjectId(id);
    setShowProject(true);
  };

  const [showTaskWindow, setShowTaskWindow] = useState(false);
  const [taskId, setTaskId] = useState(-1);
  const openTaskWindow = (id: number) => {
    setTaskId(id);
    setShowTaskWindow(true);
  };

  const [settingsBoardId, setSettingsBoardId] = useState(-1);
  const [showBoardSettingsWindow, setShowBoardSettingsWindow] = useState(false);
  const openBoardSettingsWindow = (id: number) => {
    setSettingsBoardId(id);
    setShowBoardSettingsWindow(true);
  };

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
          setShowCreateBoard={setShowCreateBoard}
          boardNameList={boardNameList}
          boardMap={boardMap}
          setBoardId={setBoardId}
          openBoardSettingsWindow={openBoardSettingsWindow}
        />
        <Board
          sidebarOpen={sidebarOpen}
          setShowCreateTask={setShowCreateTask}
          openProjectWindow={openProjectWindow}
          openTaskWindow={openTaskWindow}
          boardId={boardId}
        />
        {showCreateBoardWindow ? (
          <CreateBoardWindow setShowCreateBoard={setShowCreateBoard} />
        ) : null}
        {showCreateTaskWindow ? (
          <CreateTaskWindow setShowCreateTask={setShowCreateTask} />
        ) : null}
        {showProjectWindow ? (
          <ProjectWindow
            projectId={projectId}
            setShowProject={setShowProject}
          />
        ) : null}
        {showTaskWindow ? (
          <TaskWindow taskId={taskId} setShowTaskWindow={setShowTaskWindow} />
        ) : null}
        {showBoardSettingsWindow ? (
          <BoardSettingsWindow
            setShowBoardSettingsWindow={setShowBoardSettingsWindow}
          />
        ) : null}
      </div>
    </>
  );
};
