import axios from "axios";
import IJwtAccessTokens from "../interfaces/IJwtTokens";

export const API_URL = "http://localhost:8088/api/v1";

const $api = axios.create({
  withCredentials: true,
  baseURL: API_URL,
});

$api.interceptors.request.use((config) => {
  if (config.url && config.url.startsWith("/auth")) {
    return config;
  }
  const accessToken = localStorage.getItem("access_token");
  if (!accessToken) {
    return config;
  }
  if (!config.headers) {
    throw new Error("Config is null or undefined in request interceptor");
  }

  config.headers.Authorization = `Bearer ${localStorage.getItem("access_token")}`;
  return config;
});

$api.interceptors.response.use(
  (config) => config,
  async (error) => {
    if (
      error.response.status === 403 &&
      error.config &&
      !error.config._isRetry
    ) {
      const originalRequest = error.config;
      originalRequest._isRetry = true;
      try {
        const response = await axios.get<IJwtAccessTokens>(
          `${API_URL}/auth/update_tokens`,
          {
            withCredentials: true,
          },
        );
        localStorage.setItem("access_token", response.headers["authorization"]);
        return $api.request(originalRequest);
      } catch (error) {
        console.log("Authentication required");
      }
    }
    throw error;
  },
);

export default $api;
