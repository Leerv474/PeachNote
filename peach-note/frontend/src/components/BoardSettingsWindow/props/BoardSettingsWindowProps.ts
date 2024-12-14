export default interface BoardSettignsWindowProps {
  currentBoardId: number;
  setCurrentBoardId: React.Dispatch<React.SetStateAction<number>>;
  settingsBoardId: number;
  setShowBoardSettingsWindow: React.Dispatch<React.SetStateAction<boolean>>;
  triggerBoardListReload: React.Dispatch<React.SetStateAction<number>>;
  triggerBoardReload: React.Dispatch<React.SetStateAction<boolean>>;
}
