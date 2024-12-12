export default interface BoardSettignsWindowProps {
  settingsBoardId: number;
  setShowBoardSettingsWindow: React.Dispatch<React.SetStateAction<boolean>>;
  triggerBoardListReload: React.Dispatch<React.SetStateAction<number>>;
}
