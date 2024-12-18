import $api from "../http";
import ISignInRequest from "../interfaces/ISignInRequest";
import ISignUpRequest from "../interfaces/ISignUpRequest";
import IUser from "../interfaces/IUser";
import IUserData from "../interfaces/IUserData";

export default class UserService {
  static async changePassword(password: string) {
    const response = await $api.post("/user/change_password", {
      ...{ password: password },
    });
    localStorage.setItem("access_token", response.headers["authorization"]);
  }
  static async changeUsername(username: string) {
    await $api.post("/user/rename", { ...{ username: username } });
  }

  static async changeEmail(email: string) {
    const response = await $api.post("/user/change_email", {
      ...{ email: email },
    });
    localStorage.setItem("access_token", response.headers["authorization"]);
  }

  static async viewData() {
    return await $api.get<IUserData>(`/user/view_user_data`);
  }

  static async signUp({ username, email, password }: ISignUpRequest) {
    return await $api.post<IUser>("/auth/register", {
      username,
      email,
      password,
    });
  }

  static async activateAccount(code: string) {
    await $api.get(`/auth/activate_account?token=${code}`);
  }

  static async signIn(user: ISignInRequest) {
    const response = await $api.post<IUser>("/auth/login", { ...user });
    localStorage.setItem("access_token", response.headers["authorization"]);
    return response;
  }

  static async logout() {
    await $api.delete("/auth/logout");
    localStorage.removeItem("access_token");
  }

  static async usersExist(users: Array<string>) {
    const response = await $api.post<Array<number>>("/user/users_exist", {
      ...users,
    });
    return response;
  }

  static async getUsername() {
    return await $api.get<IUser>("/user/get_username");
  }
}
