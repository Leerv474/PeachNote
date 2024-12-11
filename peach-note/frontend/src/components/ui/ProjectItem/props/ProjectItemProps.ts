export default interface ProjectItemProps {
  projectId: number;
  title: string;
  tasksAmount: number;
  finishedTasksAmount: number;
  openProjectWindow: (id: number) => void;
}
