import $api from "../http";
import ITaskCreateResponse from "../interfaces/ITaskCreateResponse";
import ITaskDataDto from "../interfaces/ITaskDataDto";
import ITaskDto from "../interfaces/ITaskDto";
import ITaskUpdateRequest from "../interfaces/ITaskUpdateRequest";

export default class TaskService {
  static async create(taskDto: ITaskDto) {
    const response = await $api.post<ITaskCreateResponse>("/task/create", {
      ...taskDto,
    });
    return response;
  }

  static async convertToProject(taskId: number) {
    return await $api.get(`/task/convertToProject/${taskId}`);
  }

  static async putInAwait(taskId: number) {
    return await $api.get(`/task/putInAwait/${taskId}`);
  }

  static async putInDelayed(taskId: number) {
    return await $api.get(`/task/putInDelayed/${taskId}`);
  }

  static async putInCurrent(taskId: number) {
    return await $api.get(`/task/putInCurrent/${taskId}`);
  }

  static async promoteStatus(taskId: number) {
    return await $api.get(`/task/promoteStatus/${taskId}`);
  }

  static async viewData(taskId: number) {
    return await $api.get<ITaskDataDto>(`/task/view_data/${taskId}`);
  }

  static async edit(requestData: ITaskUpdateRequest) {
    return await $api.post("/task/edit", { ...requestData });
  }

  static async delete(taskId: number) {
    return await $api.get(`/task/delete/${taskId}`);
  }
}
