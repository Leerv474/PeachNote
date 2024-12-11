import React, { useEffect, useRef, useState } from "react";
import style from "./CreateBoardWindow.module.css";
import classNames from "classnames";
import CreateBoardWindowProps from "./props/CreateBoardWindowProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import { ActionButton } from "../ui/ActionButton/ActionButton";

export const CreateBoardWindow: React.FC<CreateBoardWindowProps> = ({
  setShowCreateBoard,
}) => {
  const [tableNameInputs, setTableNameInputs] = useState<string[]>([]);
  const [users, setUsers] = useState<string[]>([]);
  const [newUserInfo, setNewUserInfo] = useState("");

  const scrollableContainerRef = useRef<HTMLDivElement>(null);

  const addInput = () => {
    setTableNameInputs([...tableNameInputs, ""]);
  };

  const addUser = () => {
    setUsers([...users, newUserInfo]);
    setNewUserInfo("");
  };

  useEffect(() => {
    setTimeout(
      () =>
        scrollableContainerRef.current
          ? (scrollableContainerRef.current.scrollTop =
              -scrollableContainerRef.current.scrollHeight)
          : null,
      300,
    );
  }, [users]);

  return (
    <>
      <ModelWindow setShowWindow={setShowCreateBoard}>
        <div className={classNames(style.top_bar)}>
          <input
            type="text"
            placeholder={"board name"}
            className={classNames(style.title_input)}
          />
        </div>
        <div className={classNames(style.contents)}>
          <div className={classNames(style.table_options_container)}>
            <p>table options</p>
            <div className={classNames(style.options)}>
              <div className={classNames(style.tables_list)}>
                {tableNameInputs.map((placeholder, index) => (
                  <div key={index} className={classNames(style.new_table_item)}>
                    <input
                      type="text"
                      onChange={(e) => {
                        setTableNameInputs((prev) =>
                          prev.map((value, i) => {
                            if (i !== index) return value;
                            value = e.target.value;
                            return value;
                          }),
                        );
                      }}
                      placeholder="status name"
                      value={tableNameInputs[index]}
                    />
                  </div>
                ))}
                <button
                  type="button"
                  onClick={() => {
                    if (tableNameInputs.lastIndexOf("") !== -1 && tableNameInputs.length !== 0)
                      return;
                    addInput();
                  }}
                >
                  new
                </button>
              </div>
              <div className={classNames(style.tables_list, style.static)}>
                <div className={classNames(style.table_item_static)}>
                  <p>Bucket</p>
                </div>
                <div className={classNames(style.table_item_static)}>
                  <p>Postponed</p>
                </div>
                <div className={classNames(style.table_item_static)}>
                  <p>Await</p>
                </div>
                <div className={classNames(style.table_item_static)}>
                  <p>Current</p>
                </div>
                <div className={classNames(style.table_placeholder)} />
                <div className={classNames(style.table_item_static)}>
                  <p>Done</p>
                </div>
              </div>
            </div>
          </div>
          <div className={classNames(style.user_list_container)}>
            <p>permitted users</p>
            <div className={classNames(style.user_list)}>
              <div className={classNames(style.add_user_container)}>
                <input
                  type="text"
                  value={newUserInfo}
                  onChange={(e) => setNewUserInfo(e.target.value)}
                  placeholder="username"
                />
                <button
                  type="button"
                  onClick={() => newUserInfo !== "" && addUser()}
                  onKeyDown={(e) =>
                    e.key === "Enter" && newUserInfo !== "" && addUser()
                  }
                >
                  add
                </button>
              </div>
              <div
                ref={scrollableContainerRef}
                className={classNames(style.user_list_scroll_container)}
              >
                {users.map((userInfo, key) => (
                  <div key={key} className={classNames(style.user_item)}>
                    <p>{userInfo}</p>
                    <select defaultValue="viewer">
                      <option value="viewer">viewer</option>
                      <option value="editor">editor</option>
                    </select>
                    <button
                      type="button"
                      onClick={() => {
                        setUsers(users.filter((value, index) => index !== key));
                      }}
                    >
                      &#x2212;
                    </button>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
        <div className={classNames(style.bottom_bar)}>
          <div className={classNames(style.button_container)}>
            <ActionButton label="create" onClick={() => {}} />
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
