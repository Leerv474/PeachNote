import $api from "../http";
import ITaskDto from "../interfaces/ITaskDto";

export default class TaskService {
  static async create(taskDto: ITaskDto) {
    const response = await $api.post("/task/create", {...taskDto})
    return response;
  }
}
