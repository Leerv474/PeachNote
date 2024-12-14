import ITableItemResponse from "./ITableItemResponse";

export default interface ITaskProjectItem {
  taskId: number;
  title: string;
  deadline: string;
  priority: number;
  statusTable: ITableItemResponse;
} 
