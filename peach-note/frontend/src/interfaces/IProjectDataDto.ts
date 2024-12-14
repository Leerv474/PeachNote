import ITaskProjectItem from "./ITaskProjectItem";

export default interface IProjectDataDto {
  title: string;
  description: string | null;
  deadline: string | null;
  tasksAmount: number;
  finishedTasksAmount: number;
  taskList: Array<ITaskProjectItem>;
}
