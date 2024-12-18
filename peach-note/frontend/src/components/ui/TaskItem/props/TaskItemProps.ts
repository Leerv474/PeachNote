export default interface TaskItemProps {
  title: string;
  taskId: number;
  isLastStatus: boolean;
  isInBucket: boolean;
  isTaskProject: boolean;
  openOrganizeWindow: (
    id: number,
    title: string,
    isTaskProject: boolean,
  ) => void;
  openTaskWindow: (id: number) => void;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
}
