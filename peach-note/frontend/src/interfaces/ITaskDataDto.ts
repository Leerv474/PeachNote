export default interface ITaskDataDto {
  taskId: number;
  title: string;
  description: string;
  deadline: Date | null;
  projectId: number | null;
  projectTitle: string | null;
  userPermissionLevel: number;
}
