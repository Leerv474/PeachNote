import IBoard from "../../../interfaces/IBoard";

export default interface BoardProps {
  sidebarOpen: boolean;
  setShowCreateTask: React.Dispatch<React.SetStateAction<boolean>>;
  openProjectWindow: (id: number) => void;
  openTaskWindow: (id: number) => void;
  boardData: IBoard | null;
  tableReload: number;
}
