export default interface SidebarProps {
  setShowCreateBoard: React.Dispatch<React.SetStateAction<boolean>>;
  sidebarOpen: boolean;
  openBoardSettingsWindow: (id: number) => void;
  setBoardId: React.Dispatch<React.SetStateAction<number>>;
}
