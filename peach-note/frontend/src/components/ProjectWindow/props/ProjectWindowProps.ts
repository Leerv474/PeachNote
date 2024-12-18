export default interface ProjectWindowProps {
  projectId: number;
  setShowProject: React.Dispatch<React.SetStateAction<boolean>>;
  triggerProjectListReload: React.Dispatch<React.SetStateAction<number>>;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  triggerTaskListReload: React.Dispatch<React.SetStateAction<number>>;
  taskListReload: number;
  openTaskWindow: (taskId: number) => void;
  openCreateTaskWindow: (projectId: number, projectName: string) => void;
  openOrganizeTaskWindow: (taskId: number, title: string, isTaskProject: boolean) => void;
}
