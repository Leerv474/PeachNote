import React, { createContext } from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import StoreState from "./interfaces/IStoreState";
import Store from "./storage/store";

const store = new Store();

export const Context = createContext<StoreState>({ store });

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement,
);
root.render(
  <Context.Provider value={{ store }}>
    <App />
  </Context.Provider>,
);
