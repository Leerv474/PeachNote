export default interface BoardProps {
  sidebarOpen: boolean;
  setShowCreateTask: React.Dispatch<React.SetStateAction<boolean>>;
  openProjectWindow: (id: number) => void;
  openTaskWindow: (id: number) => void;
  boardId: number;
}
