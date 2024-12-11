import $api from "../http";
import ITable from "../interfaces/ITable";

export default class TableService {
  static async viewTable(tableId: number) {
    return await $api.get<ITable>(`/statusTable/simpleView/${tableId}`);
  }
}
