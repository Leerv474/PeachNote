import React, { useContext, useEffect, useState } from "react";
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
import { Context } from "../..";
import IBoard from "../../interfaces/IBoard";

export const Dashboard: React.FC = () => {
  const { store } = useContext(Context);

  const [boardId, setBoardId] = useState(-1);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  // triggers
  const [tableReload, triggerTableReload] = useState(0);
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

  const [boardData, setBoardData] = useState<IBoard>();

  useEffect(() => {
    const fetchBoardData = async () => {
      const data = await store.viewBoard(boardId);
      setBoardData(data);
    };
    if (boardId > 0) {
      fetchBoardData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [boardId]);

  return (
    <>
      <div className={classNames(style.dashboard_container)}>
        <Navbar
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
          boardTitle={boardData?.name ?? ""}
          username={"gigatester"}
        />
        <Sidebar
          sidebarOpen={sidebarOpen}
          setShowCreateBoard={setShowCreateBoard}
          setBoardId={setBoardId}
          openBoardSettingsWindow={openBoardSettingsWindow}
        />
        <Board
          sidebarOpen={sidebarOpen}
          setShowCreateTask={setShowCreateTask}
          openProjectWindow={openProjectWindow}
          openTaskWindow={openTaskWindow}
          boardData={boardData ?? null}
          tableReload={tableReload}
        />
        {showCreateBoardWindow ? (
          <CreateBoardWindow setShowCreateBoard={setShowCreateBoard} />
        ) : null}
        {showCreateTaskWindow ? (
          <CreateTaskWindow
            boardId={boardId}
            setShowCreateTask={setShowCreateTask}
            triggerTableReload={triggerTableReload}
          />
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
            settingsBoardId={settingsBoardId}
            setShowBoardSettingsWindow={setShowBoardSettingsWindow}
          />
        ) : null}
      </div>
    </>
  );
};
