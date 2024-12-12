import IBoard from "../../../interfaces/IBoard";

export default interface CreateBoardWindowProps {
  setShowCreateBoard: React.Dispatch<React.SetStateAction<boolean>>;
  triggerBoardListReload: React.Dispatch<React.SetStateAction<number>>;
  setBoardId: React.Dispatch<React.SetStateAction<number>>;
}
