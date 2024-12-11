interface StatusTable {
  tableId: number;
  name: string;
  displayOrder: number;
}

export default interface IBoard {
  boardId: number;
  name: string;
  userIdList: Array<number>;
  statusTableList: Array<StatusTable>;
  projectList: any;
}
