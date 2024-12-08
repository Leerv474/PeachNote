export default interface BoardItemProps {
  name: string;
  boardId: number;
  setBoardId: React.Dispatch<React.SetStateAction<number>>;
  openBoardSettingsWindow: (id: number) => void;
}
