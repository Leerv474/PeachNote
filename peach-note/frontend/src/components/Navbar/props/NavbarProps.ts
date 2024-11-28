export default interface NavbarProps {
  boardTitle: string;
  username: string;
  sidebarOpen: boolean;
  setSidebarOpen: React.Dispatch<React.SetStateAction<boolean>>;
};
