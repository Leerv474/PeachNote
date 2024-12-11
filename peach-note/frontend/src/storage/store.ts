import { makeAutoObservable } from "mobx";
import IBoard from "../interfaces/IBoard";
import IBoardItem from "../interfaces/IBoardItem";
import IBoardList from "../interfaces/IBoardList";
import IProjectItem from "../interfaces/IProjectItem";
import ISignInRequest from "../interfaces/ISignInRequest";
import ISignUpRequest from "../interfaces/ISignUpRequest";
import ITable from "../interfaces/ITable";
import ITaskDto from "../interfaces/ITaskDto";
import IUser from "../interfaces/IUser";
import BoardService from "../services/BoardService";
import ProjectService from "../services/ProjectService";
import TableService from "../services/TableService";
import TaskService from "../services/TaskService";
import UserService from "../services/UserService";

export default class Store {
  user = {} as IUser;
  isAuth = false;

  constructor() {
    makeAutoObservable(this);
  }

  setAuth(isAuth: boolean) {
    this.isAuth = isAuth;
  }

  setUser(user: IUser) {
    this.user = user;
  }

  async signUpUser(newUser: ISignUpRequest) {
    try {
      const response = await UserService.signUp(newUser);
      return response.data;
    } catch (error: any) {
      return error.response.data;
    }
  }

  async signInUser(user: ISignInRequest) {
    try {
      const response = await UserService.signIn(user);
      this.isAuth = true;
      this.user = response.data;
      return response.data;
    } catch (error: any) {
      return error.response.data;
    }
  }

  async activateAccount(
    code: number,
    refs: (HTMLInputElement | null)[],
    clearInputs: () => void,
    setError: (error: string) => void,
    setShowActivationWindow: (show: boolean) => void,
  ) {
    try {
      await UserService.activateAccount(code);
      setShowActivationWindow(false);
    } catch (error: any) {
      for (let i = 0; i < refs.length; i++) {
        if (refs[i]) {
          clearInputs();
        }
      }
      if (refs[0]) {
        refs[0].focus();
      }
      setError(error.response.data.error);
    }
  }

  async findAllBoards(): Promise<Array<IBoardItem> | undefined> {
    try {
      const response = await BoardService.findAllBoards();
      return response.data;
    } catch (error: any) {
      console.log(error.response?.error);
      console.log(error.response?.businessError);
      return undefined;
    }
  }

  async viewBoard(boardId: number): Promise<IBoard | undefined> {
    try {
      const response = await BoardService.viewBoard(boardId);
      return response.data;
    } catch (error: any) {
      console.log(error.response?.error);
      console.log(error.response?.error);
      return undefined;
    }
  }

  async viewTable(tableId: number): Promise<ITable | undefined> {
    try {
      const response = await TableService.viewTable(tableId);
      return response.data;
    } catch (error: any) {
      console.log(error.response?.error);
      console.log(error.response?.businessError);
      return undefined;
    }
  }

  async listProjects(
    boardId: number,
  ): Promise<Array<IProjectItem> | undefined> {
    try {
      const response = await ProjectService.listAllByBoard(boardId);
      return response.data;
    } catch (error: any) {
      console.log(error.response?.error);
      console.log(error.response?.businessError);
      return undefined;
    }
  }

  async createTask(data: ITaskDto) {
    try {
      const response = await TaskService.create(data);
      return response.data;
    } catch (error: any) {
      return "failed to create a task";
    }
  }
}
