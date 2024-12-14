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
import { OrganizeWindow } from "../../components/OrganizeWindow/OrganizeWindow";
import ITaskOrganizeData from "../../interfaces/ITaskOrganizeData";

export const Dashboard: React.FC = () => {
  const { store } = useContext(Context);

  const [boardId, setBoardId] = useState(-1);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  //NOTE: triggers
  const [tableReload, triggerTableReload] = useState(0);
  const [boardListReload, triggerBoardListReload] = useState(0);
  const [projectListReload, triggerProjectListReload] = useState(0);
  const [taskListReload, triggerTaskListReload] = useState(0);

  //NOTE: windows
  const [showCreateBoardWindow, setShowCreateBoard] = useState(false);
  const [showCreateTaskWindow, setShowCreateTask] = useState(false);
  const [createTaskProjectId, setCreateTaskProjectId] = useState(-1);
  const [createTaskProjectName, setCreateTaskProjectName] = useState("");
  const openCreateTaskWindow = (projectId: number, projectName: string) => {
    setCreateTaskProjectId(projectId);
    setCreateTaskProjectName(projectName);
    setShowCreateTask(true);
  };

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

  const [showOrganizeWindow, setShowOrganizeWindow] = useState(false);
  const [organizeTaskData, setOrganizeTaskId] = useState<ITaskOrganizeData>();
  const openOrganizeTaskWindow = (taskId: number, title: string, isTaskProject: boolean = false) => {
    setOrganizeTaskId({ taskId: taskId, title: title, isTaskProject: isTaskProject});
    setShowOrganizeWindow(true);
  };

  // NOTE: fetching

  const [boardData, setBoardData] = useState<IBoard>();
  const [boardReload, triggerBoardReload] = useState(false);

  useEffect(() => {
    const fetchBoardData = async () => {
      const data = await store.viewBoard(boardId);
      setBoardData(data);
    };
    if (boardId > 0) {
      fetchBoardData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [boardId, boardReload]);

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
          boardListReload={boardListReload}
          openBoardSettingsWindow={openBoardSettingsWindow}
        />
        <Board
          sidebarOpen={sidebarOpen}
          openCreateTaskWindow={openCreateTaskWindow}
          openProjectWindow={openProjectWindow}
          openTaskWindow={openTaskWindow}
          openOrganizeWindow={openOrganizeTaskWindow}
          boardData={boardData ?? null}
          tableReload={tableReload}
          triggerTableReload={triggerTableReload}
          projectListReload={projectListReload}
        />
        {showCreateBoardWindow ? (
          <CreateBoardWindow
            setShowCreateBoard={setShowCreateBoard}
            setBoardId={setBoardId}
            triggerBoardListReload={triggerBoardListReload}
          />
        ) : null}
        {showProjectWindow ? (
          <ProjectWindow
            projectId={projectId}
            setShowProject={setShowProject}
            triggerProjectListReload={triggerProjectListReload}
            triggerTableReload={triggerTableReload}
            triggerTaskListReload={triggerTaskListReload}
            taskListReload={taskListReload}
            openTaskWindow={openTaskWindow}
            openCreateTaskWindow={openCreateTaskWindow}
            openOrganizeTaskWindow={openOrganizeTaskWindow}
          />
        ) : null}
        {showTaskWindow ? (
          <TaskWindow
            taskId={taskId}
            setShowTaskWindow={setShowTaskWindow}
            openProjectWindow={openProjectWindow}
            triggerTableReload={triggerTableReload}
            triggerProjectListReload={triggerProjectListReload}
            triggerTaskListReload={triggerTaskListReload}
          />
        ) : null}
        {showBoardSettingsWindow ? (
          <BoardSettingsWindow
            settingsBoardId={settingsBoardId}
            setShowBoardSettingsWindow={setShowBoardSettingsWindow}
            triggerBoardListReload={triggerBoardListReload}
            currentBoardId={boardId}
            setCurrentBoardId={setBoardId}
            triggerBoardReload={triggerBoardReload}
          />
        ) : null}
        {showCreateTaskWindow ? (
          <CreateTaskWindow
            boardId={boardId}
            createTaskProjectId={createTaskProjectId}
            createTaskProjectName={createTaskProjectName}
            setShowCreateTask={setShowCreateTask}
            triggerTableReload={triggerTableReload}
            openOrganizeTaskWindow={openOrganizeTaskWindow}
            triggerTaskListReload={triggerTaskListReload}
          />
        ) : null}
        {showOrganizeWindow ? (
          <OrganizeWindow
            taskData={organizeTaskData}
            setShowOrganizeWindow={setShowOrganizeWindow}
            triggerTableReload={triggerTableReload}
            triggerProjectListReload={triggerProjectListReload}
            triggerTaskListReload={triggerTaskListReload}
          />
        ) : null}
      </div>
    </>
  );
};
