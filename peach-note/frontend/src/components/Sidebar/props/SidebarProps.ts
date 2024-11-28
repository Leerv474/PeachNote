export default interface SidebarProps {
  boardNameList: Array<string>;
  boardMap: Record<string, number>;
  setCreateBoard: React.Dispatch<React.SetStateAction<boolean>>;
  sidebarOpen: boolean;
}
