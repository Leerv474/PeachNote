export default interface TaskItemProps {
  title: string;
  taskId: number;
  isLastStatus: boolean;
  openTaskWindow: (id: number) => void;
}
