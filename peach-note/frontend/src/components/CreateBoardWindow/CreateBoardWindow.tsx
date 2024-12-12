import React, { useEffect, useRef, useState } from "react";
import style from "./CreateBoardWindow.module.css";
import classNames from "classnames";
import CreateBoardWindowProps from "./props/CreateBoardWindowProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import UserService from "../../services/UserService";
import BoardService from "../../services/BoardService";
import IBoardCreateRequest from "../../interfaces/IBoardCreateRequest";
import { FaPlus } from "react-icons/fa";
import { TbArrowBadgeDownFilled, TbArrowBadgeUpFilled } from "react-icons/tb";
import { IoIosRemoveCircle } from "react-icons/io";
import IUserPermissionRequest from "../../interfaces/IUserPermissionRequest";

export const CreateBoardWindow: React.FC<CreateBoardWindowProps> = ({
  setShowCreateBoard,
  triggerBoardListReload,
  setBoardId,
}) => {
  const [tableNameList, setTableNameList] = useState<string[]>([]);
  const [newTableName, setNewTableName] = useState("");
  const [users, setUsers] = useState<Array<IUserPermissionRequest>>([]);
  const [newUserInfo, setNewUserInfo] = useState("");
  const handlePermissionUpdate = (
    e: React.ChangeEvent<HTMLSelectElement>,
    key: number,
    username: string,
  ) => {
    const updatedPermissionValue = Number(e.target.value);

    const newValue = {
      username: username || "giganoone",
      permissionLevel: updatedPermissionValue,
    };

    setUsers((prevValues) =>
      prevValues.map((value, i) => (i === key ? newValue : value)),
    );
  };

  const moveUp = (index: number, items: Array<any>) => {
    if (index > 0) {
      const updatedItems = [...items];
      [updatedItems[index], updatedItems[index - 1]] = [
        updatedItems[index - 1],
        updatedItems[index],
      ];
      return updatedItems;
    }
  };

  const moveDown = (index: number, items: Array<any>) => {
    if (index < items.length - 1) {
      const updatedItems = [...items];
      [updatedItems[index], updatedItems[index + 1]] = [
        updatedItems[index + 1],
        updatedItems[index],
      ];
      return updatedItems;
    }
  };

  const scrollableContainerRef = useRef<HTMLDivElement>(null);

  const titleInputRef = useRef<HTMLInputElement>(null);

  const addUser = () => {
    setUsers([...users, { username: newUserInfo, permissionLevel: 1 }]);
    setNewUserInfo("");
  };

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [disappear, setDisappear] = useState(false);
  const handleMessageDisappearAnimation = () => {
    setTimeout(() => {
      setDisappear(true);
    }, 1000);
    setTimeout(() => {
      setErrorMessage("");
      setSuccessMessage("");
      setDisappear(false);
    }, 1300);
  };

  const validateBoardName = (tableName: string | undefined) => {
    if (tableName) {
      if (tableName.length >= 2 && tableName.length <= 32) {
        return true;
      }
      if (tableName.length < 2) {
        setErrorMessage("board name is too short");
      }
      if (tableName.length > 32) {
        setErrorMessage("board name is too long");
      }
      handleMessageDisappearAnimation();
      return false;
    } else {
      setErrorMessage("board name required");
      handleMessageDisappearAnimation();
      return false;
    }
  };

  const validateTableNames = () => {
    const invalid = tableNameList.some(
      (item, index, self) => self.indexOf(item) !== index || item === "",
    );
    if (invalid) {
      setErrorMessage("duplicate tables are not allowed");
      handleMessageDisappearAnimation();
    }
    return invalid;
  };

  const handleBoardCreate = async () => {
    const boardName = titleInputRef.current?.value;
    if (!validateBoardName(boardName) || validateTableNames()) {
      return;
    }
    try {
      const boardRequest: IBoardCreateRequest = {
        name: boardName || "",
        additionalStatusList: tableNameList.reverse() || null,
        userList: users || null,
      };
      const response = await BoardService.create(boardRequest);
      const boardData = response.data;
      triggerBoardListReload((prev) => prev + 1);
      setBoardId(boardData.boardId);
      setShowCreateBoard(false);
    } catch (error: any) {
      const errorMessage = error.response;
      setErrorMessage(
        errorMessage?.error || errorMessage.businessError || "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
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
      <ModelWindow
        setShowWindow={setShowCreateBoard}
        errorMessage={errorMessage}
        successMessage={successMessage}
        disappear={disappear}
      >
        <div className={classNames(style.top_bar)}>
          <input
            type="text"
            ref={titleInputRef}
            placeholder={"board name"}
            className={classNames(style.title_input)}
          />
        </div>
        <div className={classNames(style.contents)}>
          <div className={classNames(style.table_options_container)}>
            <p>table options</p>
            <div className={classNames(style.options)}>
              <div className={classNames(style.tables_list)}>
                <div className={classNames(style.add_table_container)}>
                  <input
                    type="text"
                    placeholder="status name"
                    value={newTableName}
                    onChange={(e) => setNewTableName(e.target.value)}
                  />
                  <button
                    type="button"
                    onClick={() => {
                      if (newTableName === "") {
                        return;
                      }
                      setTableNameList([...tableNameList, newTableName]);
                      setNewTableName("");
                    }}
                  >
                    <FaPlus className={classNames(style.plus_icon)} />
                  </button>
                </div>
                <div className={classNames(style.table_list_scroll_container)}>
                  {tableNameList.map((name, key) => (
                    <div key={key} className={classNames(style.new_table_item)}>
                      <p>{name}</p>
                      <div className={classNames(style.move_buttons_container)}>
                        <button
                          type="button"
                          onClick={() => {
                            setTableNameList(
                              moveDown(key, tableNameList) || tableNameList,
                            );
                          }}
                        >
                          <TbArrowBadgeUpFilled />
                        </button>
                        <button
                          type="button"
                          onClick={() => {
                            setTableNameList(
                              moveUp(key, tableNameList) || tableNameList,
                            );
                          }}
                        >
                          <TbArrowBadgeDownFilled />
                        </button>
                      </div>
                      <button
                        type="button"
                        className={classNames(style.remove_button)}
                        onClick={() => {
                          setTableNameList(
                            tableNameList.filter(
                              (value, index) => index !== key,
                            ),
                          );
                        }}
                      >
                        <IoIosRemoveCircle />
                      </button>
                    </div>
                  ))}
                </div>
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
                  <FaPlus className={classNames(style.plus_icon)} />
                </button>
              </div>
              <div
                ref={scrollableContainerRef}
                className={classNames(style.user_list_scroll_container)}
              >
                {users.map((userInfo, key) => (
                  <div key={key} className={classNames(style.user_item)}>
                    <p>{userInfo?.username}</p>
                    <select
                      defaultValue={userInfo.permissionLevel}
                      onChange={(e) => {
                        handlePermissionUpdate(
                          e,
                          key,
                          userInfo?.username || "giganoone",
                        );
                      }}
                    >
                      <option value="1">viewer</option>
                      <option value="2">editor</option>
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
            <ActionButton label="create" onClick={handleBoardCreate} />
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
