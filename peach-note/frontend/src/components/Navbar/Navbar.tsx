import React, { useState } from "react";
import style from "./Navbar.module.css";
import classNames from "classnames";
import NavbarProps from "./props/NavbarProps";
import { RiSidebarFoldFill, RiSidebarUnfoldFill } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import { ActionButton } from "../ui/ActionButton/ActionButton";

export const Navbar: React.FC<NavbarProps> = ({
  boardTitle,
  username,
  sidebarOpen,
  setSidebarOpen,
}) => {
  const navigate = useNavigate();
  const handleProfileRedirect = () => {
    navigate("/profile");
  };
  return (
    <>
      <div className={classNames(style.navbar)}>
        <div className={classNames(style.board_title)}>
          <h2>{boardTitle}</h2>
        </div>
        <div
          className={classNames(style.main_bar, {
            [style.squeeze]: sidebarOpen,
          })}
        >
          <div
            className={classNames(style.sidebar_button)}
            onClick={() => {
              setSidebarOpen(!sidebarOpen);
            }}
          >
            {sidebarOpen ? (
              <RiSidebarFoldFill className={classNames(style.icon)} />
            ) : (
              <RiSidebarUnfoldFill className={classNames(style.icon)} />
            )}
          </div>

          <div className={style.title_box}>
            <h1>
              PeachN
              <img src="./peachnote-icon.png" alt="PEACH" />
              te
            </h1>
          </div>
          <ActionButton
            classname={style.profile_button_pos}
            label={username}
            onClick={handleProfileRedirect}
          />
        </div>
      </div>
    </>
  );
};
