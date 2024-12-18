import { observer } from "mobx-react-lite";
import React, { useContext, useEffect } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Context } from ".";
import { AuthPage } from "./pages/AuthPage/AuthPage";
import { Dashboard } from "./pages/Dashboard/Dashboard";
import { ProfilePage } from "./pages/ProfilePage/ProfilePage";
import { WelcomePage } from "./pages/WelcomePage/WelcomePage";

function App() {
  const { store } = useContext(Context);

  //  TODO: reload behaviour
  useEffect(() => {
    if (localStorage.getItem("access_token")) {
      store.loadUser();
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (!store.isAuth) {
    return (
      <div className="root">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<WelcomePage />} />
            <Route path="/auth" element={<AuthPage />} />
            <Route
              path="/dashboard"
              element={<Navigate to="/" replace />}
            />
            <Route path="/profile" element={<Navigate to="/auth" replace />} />
          </Routes>
        </BrowserRouter>
      </div>
    );
  }

  return (
    <div className="root">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/auth" element={<Navigate to="/profile" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<ProfilePage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default observer(App);
