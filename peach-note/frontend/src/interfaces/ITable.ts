import ITaskItem from "./ITaskItem";

export default interface ITable {
  tableId: number;
  name: string;
  taskList: Array<ITaskItem>;
}
