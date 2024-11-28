import React, { useState } from "react";
import style from "./ProjectList.module.css";
import classNames from "classnames";
import { TiArrowSortedUp, TiArrowSortedDown } from "react-icons/ti";

export const ProjectList: React.FC = () => {
  const [listExpanded, setListExpanded] = useState(true);
  return (
    <>
      <div
        className={classNames(style.container, {
          container_expanded: listExpanded,
        })}
      >
        <div
          className={classNames(style.button_container, {
            [style.button_container_expanded]: listExpanded,
          })}
        >
          <div
            onClick={() => {
              setListExpanded(!listExpanded);
            }}
            className={classNames(style.expand_button, {
              [style.expand_button_expanded]: listExpanded,
            })}
          >
            <TiArrowSortedUp
              className={classNames(style.arrow, {
                [style.arrow_rotated]: listExpanded,
              })}
            />
            <p>Projects</p>
          </div>
        </div>
        <div
          className={classNames(style.list_container, {
            [style.list_expanded]: listExpanded,
          })}
        >
          <div className={classNames(style.scroll_container)}>
            <p>project</p>
            <p>project</p>
            <p>project</p>
            <p>project</p>
            <p>project</p>
            <p>project</p>
          </div>
        </div>
      </div>
    </>
  );
};
