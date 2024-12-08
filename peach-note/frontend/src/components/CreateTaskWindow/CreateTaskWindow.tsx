import React from "react";
import style from "./CreateTaskWindow.module.css";
import classNames from "classnames";
import CreateTaskProps from "./props/CreateTaskProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import { ActionButton } from "../ui/ActionButton/ActionButton";

export const CreateTaskWindow: React.FC<CreateTaskProps> = ({
  setShowCreateTask,
}) => {
  const today = new Date();
  const minDate = today.toISOString().split("T")[0]; // Format to 'YYYY-MM-DD'
  return (
    <>
      <ModelWindow setShowWindow={setShowCreateTask} windowSizeCss={classNames(style.window_size)}>
        <div className={classNames(style.top_bar)}>
          <input
            type="text"
            placeholder="task title"
            className={classNames(style.title_input)}
          />
          <input
            type="date"
            className={classNames(style.deadline_input)}
            min={minDate}
          />
        </div>
        <div className={classNames(style.contents)}>
          <p className={classNames(style.description_title)}>Description</p>
          <textarea
            placeholder="write down any important tips on the task..."
            maxLength={512}
            className={classNames(style.description)}
          />
        </div>
        <div className={classNames(style.bottom_bar)}>
          <div className={classNames(style.create_button_container)}>
            <ActionButton label="create" onClick={() => {}} />
          </div>
          <div className={classNames(style.organize_button_container)}>
            <ActionButton label="organize" onClick={() => {}} />
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
