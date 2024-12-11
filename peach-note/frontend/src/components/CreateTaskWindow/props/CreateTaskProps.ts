export default interface CreateTaskProps {
  boardId: number;
  setShowCreateTask: React.Dispatch<React.SetStateAction<boolean>>;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
}
