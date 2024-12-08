import React, { useState } from "react";
import style from "./ModelWindow.module.css";
import classNames from "classnames";
import { ActionButton } from "../ActionButton/ActionButton";
import ModelWindowProps from "./props/ModelWindowProps";

export const ModelWindow: React.FC<ModelWindowProps> = ({
  windowSizeCss,
  setShowWindow,
  children,
}) => {
  const [closing, setClosing] = useState(false);
  const handleClose = () => {
    setClosing(true);
    setTimeout(() => {
      setShowWindow(false);
    }, 300);
  };
  return (
    <>
      <div className={classNames(style.window_container)}>
        <div
          className={classNames(style.back_area)}
          onClick={handleClose}
        ></div>
        <div
          className={classNames(style.window, windowSizeCss, {
            [style.window_closing]: closing,
          })}
        >
          <button
            className={classNames(style.close_button)}
            type="button"
            onClick={handleClose}
          >
            &#x2718;
          </button>
          {children}
        </div>
      </div>
    </>
  );
};
