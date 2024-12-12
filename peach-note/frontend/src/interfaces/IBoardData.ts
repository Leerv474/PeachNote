import IAdditionalTableDto from "./IAdditionalTableDto";
import IUserPermissionRequest from "./IUserPermissionRequest";

export default interface IBoardData {
  boardId: number;
  name: string;
  currentUserPermissionLevel: number;
  userPermissionList: Array<IUserPermissionRequest>;
  statusTableList: Array<IAdditionalTableDto>;
}
