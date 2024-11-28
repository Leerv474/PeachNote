import React from "react";
import style from "./ActionButton.module.css";
import classNames from "classnames";
import ActionButtonProps from "./props/ActionButtonProps"

export const ActionButton: React.FC<ActionButtonProps> = ({onClick, label, classname}) => {
  return (
    <>
      <div className={classNames(style.action_button, classname)} onClick={onClick}>
        {label}
      </div>
    </>
  );
};
