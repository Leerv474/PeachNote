import React, { useEffect, useRef, useState } from "react";
import style from "./BoardSettingsWindow.module.css";
import classNames from "classnames";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import BoardSettignsWindowProps from "./props/BoardSettingsWindowProps";
import IBoardCreateRequest from "../../interfaces/IBoardCreateRequest";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { FaPlus } from "react-icons/fa";
import IUserPermissionRequest from "../../interfaces/IUserPermissionRequest";
import { TbArrowBadgeDownFilled, TbArrowBadgeUpFilled } from "react-icons/tb";
import { IoIosRemoveCircle } from "react-icons/io";
import BoardService from "../../services/BoardService";
import IBoardUpdateRequest from "../../interfaces/IBoardUpdateRequest";
import IAdditionalTableDto from "../../interfaces/IAdditionalTableDto";

export const BoardSettingsWindow: React.FC<BoardSettignsWindowProps> = ({
  settingsBoardId,
  setShowBoardSettingsWindow,
  triggerBoardListReload,
}) => {
  //NOTE: fetching data
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await BoardService.viewBoardData(settingsBoardId);
        const boardData = response.data;
        setTableList(boardData.statusTableList);
        setUsers(boardData.userPermissionList || []);
        setCurrentUserPermissionLevel(
          boardData.currentUserPermissionLevel || 0,
        );
        setBoardName(boardData.name);
      } catch (error: any) {
        const errorResponse = error.response;
        setErrorMessage(
          errorResponse?.error ||
            errorResponse?.businessError ||
            "unexpected error",
        );
        handleMessageDisappearAnimation();
      }
    };
    fetchData();
  }, []);

  const [boardName, setBoardName] = useState("");
  const [currentUserPermissionLevel, setCurrentUserPermissionLevel] =
    useState(0);
  const [tableList, setTableList] = useState<IAdditionalTableDto[]>([]);
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
    const invalid = tableList.some(
      (item, index, self) => self.indexOf(item) !== index || item.name === "",
    );
    if (invalid) {
      setErrorMessage("duplicate tables are not allowed");
      handleMessageDisappearAnimation();
    }
    return invalid;
  };

  const handleBoardSave = async () => {
    if (currentUserPermissionLevel !== 3) {
      setErrorMessage("only creator can edit board options");
      handleMessageDisappearAnimation();
      return;
    }
    const boardName = titleInputRef.current?.value;
    if (!validateBoardName(boardName) || validateTableNames()) {
      return;
    }
    try {
      const boardRequest: IBoardUpdateRequest = {
        boardId: settingsBoardId,
        name: boardName || "",
        additionalStatusList: tableList.reverse() || null,
        userList: users || null,
      };
      const response = await BoardService.save(boardRequest);
      const boardData = response.data;
      triggerBoardListReload((prev) => prev + 1);
      setShowBoardSettingsWindow(false);
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
        setShowWindow={setShowBoardSettingsWindow}
        errorMessage={errorMessage}
        successMessage={successMessage}
        disappear={disappear}
      >
        <div className={classNames(style.top_bar)}>
          <input
            type="text"
            ref={titleInputRef}
            disabled={currentUserPermissionLevel !== 3}
            placeholder={boardName}
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
                    disabled={currentUserPermissionLevel !== 3}
                    placeholder="status name"
                    value={newTableName}
                    onChange={(e) => setNewTableName(e.target.value)}
                  />
                  <button
                    type="button"
                    disabled={currentUserPermissionLevel !== 3}
                    onClick={() => {
                      if (newTableName === "") {
                        return;
                      }
                      setTableList([
                        ...tableList,
                        { tableId: -1, name: newTableName },
                      ]);
                      setNewTableName("");
                    }}
                  >
                    <FaPlus className={classNames(style.plus_icon)} />
                  </button>
                </div>
                <div className={classNames(style.table_list_scroll_container)}>
                  {tableList.map((item, key) => (
                    <div key={key} className={classNames(style.new_table_item)}>
                      <p>{item.name}</p>
                      <div className={classNames(style.move_buttons_container)}>
                        <button
                          type="button"
                          disabled={currentUserPermissionLevel !== 3}
                          onClick={() => {
                            setTableList(
                              moveDown(key, tableList) || tableList,
                            );
                          }}
                        >
                          <TbArrowBadgeUpFilled />
                        </button>
                        <button
                          type="button"
                          disabled={currentUserPermissionLevel !== 3}
                          onClick={() => {
                            setTableList(
                              moveUp(key, tableList) || tableList,
                            );
                          }}
                        >
                          <TbArrowBadgeDownFilled />
                        </button>
                      </div>
                      <button
                        type="button"
                        className={classNames(style.remove_button)}
                        disabled={currentUserPermissionLevel !== 3}
                        onClick={() => {
                          setTableList(
                            tableList.filter(
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
                  disabled={currentUserPermissionLevel !== 3}
                  onChange={(e) => setNewUserInfo(e.target.value)}
                  placeholder="username"
                />
                <button
                  type="button"
                  disabled={currentUserPermissionLevel !== 3}
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
                    <p className={classNames(style.username)}>
                      {userInfo?.username}
                    </p>
                    {userInfo.permissionLevel === 3 ? (
                      <p className={classNames(style.creator_permission)}>
                        creator
                      </p>
                    ) : (
                      <select
                        defaultValue={userInfo.permissionLevel}
                        disabled={currentUserPermissionLevel !== 3}
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
                    )}
                    {userInfo.permissionLevel !== 3 &&
                    userInfo.permissionLevel !== currentUserPermissionLevel ? (
                      <button
                        type="button"
                        disabled={currentUserPermissionLevel !== 3}
                        onClick={() => {
                          setUsers(
                            users.filter((value, index) => index !== key),
                          );
                        }}
                      >
                        &#x2212;
                      </button>
                    ) : null}
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
        <div className={classNames(style.bottom_bar)}>
          <div className={classNames(style.button_container)}>
            <ActionButton label="save" onClick={handleBoardSave} />
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
