export default interface TaskItemProps {
  title: string;
  taskId: number;
  isLastStatus: boolean;
  isInBucket: boolean;
  openOrganizeWindow: (id: number, title: string) => void;
  openTaskWindow: (id: number) => void;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
}
