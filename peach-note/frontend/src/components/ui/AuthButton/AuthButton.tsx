import style from "./AuthButton.module.css";
import React from "react";
import classNames from "classnames";
import props from "./props/AuthButtonProps";

export const AuthButton: React.FC<props> = ({ label, onClick}) => {
  return (
    <>
      <div className={classNames(style.auth_button)} onClick={onClick}>{label}</div>
    </>
  );
};
