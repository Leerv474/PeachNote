import IAdditionalTableDto from "./IAdditionalTableDto";
import IUserPermissionRequest from "./IUserPermissionRequest";

export default interface IBoardCreateRequest {
  boardId: number;
  name: string;
  additionalStatusList: Array<IAdditionalTableDto> | null;
  userList: Array<IUserPermissionRequest> | null;
}
