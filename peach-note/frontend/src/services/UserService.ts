import $api from "../http";
import ISignInRequest from "../interfaces/ISignInRequest";
import ISignUpRequest from "../interfaces/ISignUpRequest";
import IUser from "../interfaces/IUser";

export default class UserService {
  static async signUp({ username, email, password }: ISignUpRequest) {
    return await $api.post<IUser>("/auth/register", {
      username,
      email,
      password,
    });
  }

  static async activateAccount(code: number) {
    return await $api.get(`/auth/activate_account?token=${code}`);
  }

  static async signIn(user: ISignInRequest) {
    const response = await $api.post<IUser>("/auth/login", { ...user });
    localStorage.setItem("access_token", response.headers["authorization"]);
    return response;
  }

  static async logout() {
    return await $api.delete("/user/logout");
  }
}