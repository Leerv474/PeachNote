import IUser from "./IUser";

export default interface IJwtAccessTokens {
  accessToken: string;
  userData: IUser;
}
