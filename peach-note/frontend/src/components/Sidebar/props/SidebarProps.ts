export default interface SidebarProps {
  boardNameList: Array<string>;
  boardMap: Record<string, number>;
  setShowCreateBoard: React.Dispatch<React.SetStateAction<boolean>>;
  sidebarOpen: boolean;
  openBoardSettingsWindow: (id: number) => void;
  setBoardId: React.Dispatch<React.SetStateAction<number>>;
}
