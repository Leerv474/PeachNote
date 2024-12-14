import style from "./ProfilePage.module.css";
import React, { useState } from "react";
import classNames from "classnames";
import { FaPencil } from "react-icons/fa6";
import { FaSave } from "react-icons/fa";
import { TiCancel } from "react-icons/ti";

export const ProfilePage: React.FC = () => {
  //TODO: put real data from storage
  const username = "giga_tester";
  const email = "giga@mail.com";
  const [editUsername, setEditUsername] = useState(false);
  const [editEmail, setEditEmail] = useState(false);
  const [editPassword, setEditPassword] = useState(false);

  const [peachFalls, setPeachFalls] = useState(false);
  return (
    <>
      <div className={classNames(style.main_card)}>
        <div className={style.title_box}>
          <h1>
            PeachN
            <img
              src="./peachnote-icon.png"
              alt="PEACH"
              className={classNames(
                { [style.wobble]: !peachFalls },
                { [style.peach_falls]: peachFalls },
              )}
              onClick={() => {
                setPeachFalls(true);
                setTimeout(() => setPeachFalls(false), 2000);
              }}
            />
            te
          </h1>
        </div>
        <div className={classNames(style.page_title_bar)}>
          <p>profile</p>
        </div>
        <div className={classNames(style.card)}>
          <div className={classNames(style.user_data_container)}>
            <div className={classNames(style.user_data)}>
              <p className={classNames(style.data_title)}>username</p>
              {editUsername ? (
                <div className={classNames(style.data, style.editing)}>
                  <input
                    type="text"
                    name="username"
                    placeholder="new username"
                  />
                  <button
                    className={classNames(style.cancel_button)}
                    onClick={() => {
                      setEditUsername(false);
                    }}
                  >
                    <TiCancel />
                  </button>
                  <button className={classNames(style.button)}>
                    <FaSave />
                  </button>
                </div>
              ) : (
                <div className={classNames(style.data)}>
                  <p>{username}</p>
                  <button
                    className={classNames(style.button)}
                    onClick={() => {
                      setEditUsername(true);
                      setEditEmail(false);
                      setEditPassword(false);
                    }}
                  >
                    <FaPencil />
                  </button>
                </div>
              )}
            </div>

            <div className={classNames(style.user_data)}>
              <p className={classNames(style.data_title)}>email</p>
              {editEmail ? (
                <div className={classNames(style.data, style.editing)}>
                  <input type="email" name="email" placeholder="new email" />
                  <button
                    className={classNames(style.cancel_button)}
                    onClick={() => {
                      setEditEmail(false);
                    }}
                  >
                    <TiCancel />
                  </button>
                  <button className={classNames(style.button)}>
                    <FaSave />
                  </button>
                </div>
              ) : (
                <div className={classNames(style.data)}>
                  <p>{email}</p>
                  <button
                    className={classNames(style.button)}
                    onClick={() => {
                      setEditEmail(true);
                      setEditUsername(false);
                      setEditPassword(false);
                    }}
                  >
                    <FaPencil />
                  </button>
                </div>
              )}
            </div>

            <div className={classNames(style.user_data)}>
              <p className={classNames(style.data_title)}>password</p>
              {editPassword ? (
                <div className={classNames(style.data)}>
                  <input
                    type="password"
                    name="password"
                    placeholder="new password"
                  />

                  <button
                    className={classNames(style.cancel_button)}
                    onClick={() => {
                      setEditPassword(false);
                    }}
                  >
                    <TiCancel />
                  </button>
                  <input
                    type="password"
                    name="password"
                    placeholder="repeat new password"
                  />
                  <button className={classNames(style.button)}>
                    <FaSave />
                  </button>
                </div>
              ) : (
                <div className={classNames(style.data)}>
                  <p>{username}</p>
                  <button
                    className={classNames(style.button)}
                    onClick={() => {
                      setEditPassword(true);
                      setEditUsername(false);
                      setEditEmail(false);
                    }}
                  >
                    <FaPencil />
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
