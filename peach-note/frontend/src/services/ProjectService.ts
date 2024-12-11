import $api from "../http";
import IProjectItem from "../interfaces/IProjectItem";

export default class ProjectService {
  static async listAllByBoard(boardId: number) {
    return await $api.get<Array<IProjectItem>>(`/project/list/${boardId}`);
  }
}
