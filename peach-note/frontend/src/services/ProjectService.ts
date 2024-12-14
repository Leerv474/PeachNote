import $api from "../http";
import IProjectDataDto from "../interfaces/IProjectDataDto";
import IProjectEditRequest from "../interfaces/IProjectEditRequest";
import IProjectItem from "../interfaces/IProjectItem";

export default class ProjectService {
  static async edit(request: IProjectEditRequest) {
    return await $api.post("/project/edit", { ...request });
  }
  static async delete(projectId: number) {
    return await $api.get(`/project/delete/${projectId}`);
  }
  static async view(projectId: number) {
    return await $api.get<IProjectDataDto>(`/project/view/${projectId}`);
  }
  static async listAllByBoard(boardId: number) {
    return await $api.get<Array<IProjectItem>>(`/project/list/${boardId}`);
  }
}
