import React, { useState } from "react";
import style from "./DeleteButton.module.css";
import classNames from "classnames";
import DeleteButtonProps from "./props/DeleteButtonProps";
import { MdOutlineDeleteForever } from "react-icons/md";

export const DeleteButton: React.FC<DeleteButtonProps> = ({
  handleDelete,
  classname,
}) => {
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const handleDeleteSubmit = () => {
    setShowConfirmDelete(true);
    setTimeout(() => {
      setShowConfirmDelete(false);
    }, 3000);
  };

  return (
    <>
      <div
        className={classNames(style.button_container, classname, {
          [style.expanded]: showConfirmDelete,
        })}
      >
        {showConfirmDelete ? (
          <div className={classNames(style.confirmation_container)}>
            <p>are you sure?</p>
            <div className={classNames(style.confirnation_buttons_container)}>
              <button type="button" onClick={handleDelete}>
                delete
              </button>
            </div>
          </div>
        ) : (
          <button type="button" onClick={handleDeleteSubmit}>
            <MdOutlineDeleteForever className={classNames(style.icon)} />
          </button>
        )}
      </div>
    </>
  );
};
