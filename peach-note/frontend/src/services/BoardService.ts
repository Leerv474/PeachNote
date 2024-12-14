import $api from "../http";
import IBoard from "../interfaces/IBoard";
import IBoardCreateRequest from "../interfaces/IBoardCreateRequest";
import IBoardCreateResponse from "../interfaces/IBoardCreateResponse";
import IBoardData from "../interfaces/IBoardData";
import IBoardItem from "../interfaces/IBoardItem";
import IBoardUpdateRequest from "../interfaces/IBoardUpdateRequest";

export default class BoardService {
  static async save(boardRequest: IBoardUpdateRequest) {
    return await $api.post<IBoardCreateResponse>("/board/update", {
      ...boardRequest,
    });
  }

  static async findAllBoards() {
    return await $api.get<Array<IBoardItem>>("/board/list");
  }

  static async viewBoard(boardId: number) {
    return await $api.get<IBoard>(`/board/view/${boardId}`);
  }

  static async create(boardRequest: IBoardCreateRequest) {
    return await $api.post<IBoardCreateResponse>("/board/create", {
      ...boardRequest,
    });
  }

  static async viewBoardData(boardId: number) {
    return await $api.get<IBoardData>(`/board/view_data/${boardId}`);
  }

  static async delete(boardId: number) {
    return await $api.get(`/board/delete/${boardId}`);
  }
}
