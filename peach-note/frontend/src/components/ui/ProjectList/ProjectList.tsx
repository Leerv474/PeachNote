import React, { useState } from "react";
import style from "./ProjectList.module.css";
import classNames from "classnames";
import { TiArrowSortedUp, TiArrowSortedDown } from "react-icons/ti";

export const ProjectList: React.FC = () => {
  const [listExpanded, setListExpanded] = useState(true);
  return (
    <>
      <div className={classNames(style.list_container)}>
        <div className={classNames(style.list_title_box)}>
          <TiArrowSortedUp />
          <p>Projects</p>
        </div>
        <div
          className={classNames(style.list, style.expanded, { [style.expanded]: listExpanded })}
        >
          <div className={classNames(style.scroll)}></div>
        </div>
      </div>
    </>
  );
};
