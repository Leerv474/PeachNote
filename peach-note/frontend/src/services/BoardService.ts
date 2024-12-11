import $api from "../http";
import IBoard from "../interfaces/IBoard";
import IBoardItem from "../interfaces/IBoardItem";
import IBoardList from "../interfaces/IBoardList";
import IUser from "../interfaces/IUser";

export default class BoardService {
  static async findAllBoards() {
    return await $api.get<Array<IBoardItem>>("/board/list");
  }

  static async viewBoard(boardId: number) {
    return await $api.get<IBoard>(`/board/view/${boardId}`);
  }
}
