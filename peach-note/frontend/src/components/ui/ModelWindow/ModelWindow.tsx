import React, { useState } from "react";
import style from "./ModelWindow.module.css";
import classNames from "classnames";
import ModelWindowProps from "./props/ModelWindowProps";

export const ModelWindow: React.FC<ModelWindowProps> = ({
  windowSizeCss,
  setShowWindow,
  children,
  errorMessage,
  successMessage,
  disappear,
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
        <div className={classNames(style.message)}>
          {errorMessage ? (
            <div
              className={classNames(style.error, {
                [style.disappear]: disappear,
              })}
            >
              <p>{errorMessage}</p>
            </div>
          ) : null}
          {successMessage ? (
            <div
              className={classNames(style.success, {
                [style.disappear]: disappear,
              })}
            >
              <p>{successMessage}</p>
            </div>
          ) : null}
        </div>
      </div>
    </>
  );
};
