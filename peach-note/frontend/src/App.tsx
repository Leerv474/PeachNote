import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthPage } from "./pages/AuthPage/AuthPage";
import { Dashboard } from "./pages/Dashboard/Dashboard";
import { WelcomePage } from "./pages/WelcomePage/WelcomePage";

function App() {
  return (
    <div className="root">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          <Route path="/auth" element={<AuthPage />} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
