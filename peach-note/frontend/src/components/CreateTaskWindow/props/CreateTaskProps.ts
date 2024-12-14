export default interface CreateTaskProps {
  boardId: number;
  createTaskProjectId: number;
  createTaskProjectName: string;
  setShowCreateTask: React.Dispatch<React.SetStateAction<boolean>>;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  openOrganizeTaskWindow: (taskId: number, title: string) => void;
  triggerTaskListReload: React.Dispatch<React.SetStateAction<number>>;
}
