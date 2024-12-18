import IBoard from "../../../interfaces/IBoard";

export default interface BoardProps {
  sidebarOpen: boolean;
  openCreateTaskWindow: (projectId: number, projectName: string) => void;
  openProjectWindow: (id: number) => void;
  openTaskWindow: (id: number) => void;
  openOrganizeWindow: (id: number, title: string, isTaskProject: boolean) => void;
  boardData: IBoard | null;
  tableReload: number;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  projectListReload: number;
}
