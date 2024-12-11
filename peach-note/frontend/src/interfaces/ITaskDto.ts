export default interface ITaskDto {
  title: string;
  description: string;
  deadline: Date | null;
  projectId: number | null;
  boardId: number;
}
