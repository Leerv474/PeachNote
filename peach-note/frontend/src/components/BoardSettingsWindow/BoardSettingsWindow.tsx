import React from "react";
import style from "./BoardSettingsWindow.module.css";
import classNames from "classnames";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import BoardSettignsWindowProps from "./props/BoardSettingsWindowProps";

export const BoardSettingsWindow: React.FC<BoardSettignsWindowProps> = ({
  setShowBoardSettingsWindow
}) => {
  return (
    <>
      <ModelWindow
        setShowWindow={setShowBoardSettingsWindow}
      ></ModelWindow>
    </>
  );
};
