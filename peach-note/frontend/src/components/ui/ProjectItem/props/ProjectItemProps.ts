export default interface ProjectItemProps {
  projectId: number;
  title: string;
  tasksAmount: number;
  tasksCompleted: number;
  openProjectWindow: (id: number) => void;
}
