import IUserPermissionRequest from "./IUserPermissionRequest";

export default interface IBoardCreateRequest {
  name: string;
  additionalStatusList: Array<string> | null;
  userList: Array<IUserPermissionRequest> | null;
}
