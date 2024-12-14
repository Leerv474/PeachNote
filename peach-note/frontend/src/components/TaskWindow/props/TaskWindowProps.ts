export default interface TaskWindowProps {
  taskId: number;
  setShowTaskWindow: React.Dispatch<React.SetStateAction<boolean>>;
  openProjectWindow: (projectId: number) => void;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  triggerTaskListReload: React.Dispatch<React.SetStateAction<number>>;
  triggerProjectListReload: React.Dispatch<React.SetStateAction<number>>;
}
