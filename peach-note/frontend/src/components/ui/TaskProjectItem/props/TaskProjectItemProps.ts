export default interface TaskProjectItemProps {
  taskId: number;
  title: string;
  statusName: string;
  deadline: string;
  openTaskWindow: (taskId: number) => void;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  triggerTaskListReload: React.Dispatch<React.SetStateAction<number>>;
  setErrorMessage: React.Dispatch<React.SetStateAction<string>>;
  setSuccessMessage: React.Dispatch<React.SetStateAction<string>>;
  handleMessageDisappearAnimation: () => void;
  openOrganizeTaskWindow: (taskId: number, title: string, isTaskProject: boolean) => void;
}
