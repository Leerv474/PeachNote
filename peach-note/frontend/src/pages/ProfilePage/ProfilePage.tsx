import style from "./ProfilePage.module.css";
import * as yup from "yup";
import React, { useContext, useEffect, useState } from "react";
import classNames from "classnames";
import { FaPencil } from "react-icons/fa6";
import { FaSave } from "react-icons/fa";
import { TiCancel } from "react-icons/ti";
import { TbArrowBackUp } from "react-icons/tb";
import { Context } from "../..";
import UserService from "../../services/UserService";
import IUserData from "../../interfaces/IUserData";
import { useFormik } from "formik";
import EmailFormikProps from "./props/EmailFormikProps";
import UsernameFormikProps from "./props/UsernameFormikProps";
import PasswordFormikProps from "./props/PasswordFormikProps";
import { useNavigate } from "react-router-dom";

export const ProfilePage: React.FC = () => {
  const { store } = useContext(Context);
  const [userData, setUserData] = useState<IUserData>();

  const navigate = useNavigate();

  const [reload, triggerReload] = useState(0);

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

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await UserService.viewData();
        setUserData(response.data);
        store.setUser(
          {userId: response.data.userId, username: response.data.username}
        )
      } catch (error: any) {
        const errorMessage = error.response.data;
        setErrorMessage(
          errorMessage?.error ||
            errorMessage?.businessError ||
            "unexpected error",
        );
        handleMessageDisappearAnimation();
      }
    };
    fetchData();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [reload]);

  const usernameValidationSchema = yup.object({
    username: yup
      .string()
      .required("username is required")
      .min(2, "username is too short")
      .max(24, "username is too long")
      .test("valid username", "invalid username", (username) => {
        return new RegExp("^(?=.*[a-zA-Z])[a-zA-Z0-9_]+$").test(username);
      }),
  });

  const emailValidationSchema = yup.object({
    email: yup
      .string()
      .email("invalid email format")
      .required("email is required"),
  });

  const passwordValidationSchema = yup.object({
    password: yup
      .string()
      .required("password is required")
      .min(8, "password is too short")
      .max(128, "password is too long")
      .test("valid password", "invalid password", (password) => {
        return new RegExp("^[a-zA-Z0-9!#,.+=_-]*$").test(password);
      }),
    confirmedPassword: yup
      .string()
      .required("repeat password")
      .test(
        "valid password",
        "passwords do not match",
        function (confirmedPassword) {
          return String(this.parent.password) === confirmedPassword;
        },
      ),
  });

  const emailFormik = useFormik<EmailFormikProps>({
    initialValues: {
      email: "",
    },
    onSubmit: (values) => {
      handleEmailSave(values);
    },
    validationSchema: emailValidationSchema,
  });
  const usernameFormik = useFormik<UsernameFormikProps>({
    initialValues: {
      username: "",
    },
    onSubmit: (values) => {
      handleUsernameSave(values);
    },
    validationSchema: usernameValidationSchema,
  });
  const passwordFormik = useFormik<PasswordFormikProps>({
    initialValues: {
      password: "",
      confirmedPassword: "",
    },
    onSubmit: (values) => {
      handlePasswordSave(values);
    },
    validationSchema: passwordValidationSchema,
  });

  const handleEmailSave = async (values: EmailFormikProps) => {
    try {
      await UserService.changeEmail(values.email);
      setSuccessMessage("profile updated");
      handleMessageDisappearAnimation();
      triggerReload((prev) => prev + 1);
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error ||
          errorMessage?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
  };
  const handleUsernameSave = async (values: UsernameFormikProps) => {
    try {
      await UserService.changeUsername(values.username);
      setSuccessMessage("profile updated");
      handleMessageDisappearAnimation();
      triggerReload((prev) => prev + 1);
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error ||
          errorMessage?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
  };
  const handlePasswordSave = async (values: PasswordFormikProps) => {
    try {
      await UserService.changePassword(values.password);
      setSuccessMessage("profile updated");
      handleMessageDisappearAnimation();
      triggerReload((prev) => prev + 1);
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error ||
          errorMessage?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
  };

  const handleLogout = async () => {
    try {
      await UserService.logout();
      store.isAuth = false;
      navigate("/");
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error ||
          errorMessage?.businessError ||
          "unexpected error",
      );
      handleMessageDisappearAnimation();
    }
  };

  const handleEmailSubmit = () => {
    if (emailFormik.isValid) {
      emailFormik.submitForm();
      setEditPassword(false);
      setEditUsername(false);
      setEditEmail(false);
    } else {
      const message = emailFormik.errors.email || "unexpected formik error";
      setErrorMessage(message);
      handleMessageDisappearAnimation();
    }
  };
  const handleUsernameSubmit = () => {
    if (usernameFormik.isValid) {
      usernameFormik.submitForm();
      setEditPassword(false);
      setEditUsername(false);
      setEditEmail(false);
    } else {
      const message =
        usernameFormik.errors.username || "unexpected formik error";
      setErrorMessage(message);
      handleMessageDisappearAnimation();
    }
  };

  const handlePasswordSubmit = () => {
    if (passwordFormik.isValid) {
      passwordFormik.submitForm();
      setEditPassword(false);
      setEditUsername(false);
      setEditEmail(false);
    } else {
      const message =
        passwordFormik.errors.password ||
        passwordFormik.errors.confirmedPassword ||
        "unexpected formik error";
      setErrorMessage(message);
      handleMessageDisappearAnimation();
    }
  };
  const [editUsername, setEditUsername] = useState(false);
  const [editEmail, setEditEmail] = useState(false);
  const [editPassword, setEditPassword] = useState(false);

  const [peachFalls, setPeachFalls] = useState(false);
  return (
    <>
      <div className={classNames(style.main_card)}>
        <button
          type="button"
          className={classNames(style.back_button)}
          onClick={() => {
            navigate("/dashboard");
          }}
        >
          <TbArrowBackUp />
        </button>
        <button
          className={classNames(style.auth_button)}
          onClick={handleLogout}
        >
          logout
        </button>
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
                    onChange={usernameFormik.handleChange}
                    onBlur={usernameFormik.handleBlur}
                    value={usernameFormik.values.username}
                  />
                  <button
                    className={classNames(style.cancel_button)}
                    onClick={() => {
                      setEditUsername(false);
                    }}
                  >
                    <TiCancel />
                  </button>
                  <button
                    className={classNames(style.button)}
                    onClick={handleUsernameSubmit}
                  >
                    <FaSave />
                  </button>
                </div>
              ) : (
                <div className={classNames(style.data)}>
                  <p>{userData?.username || "giga noone"}</p>
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
                  <input
                    type="email"
                    name="email"
                    placeholder="new email"
                    onChange={emailFormik.handleChange}
                    onBlur={emailFormik.handleBlur}
                    value={emailFormik.values.email}
                  />
                  <button
                    className={classNames(style.cancel_button)}
                    onClick={() => {
                      setEditEmail(false);
                    }}
                  >
                    <TiCancel />
                  </button>
                  <button
                    className={classNames(style.button)}
                    onClick={handleEmailSubmit}
                  >
                    <FaSave />
                  </button>
                </div>
              ) : (
                <div className={classNames(style.data)}>
                  <p>{userData?.email || "giga@mail.com"}</p>
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
              <div className={classNames(style.data)}>
                {editPassword ? (
                  <>
                    <input
                      type="password"
                      name="password"
                      placeholder="new password"
                      onChange={passwordFormik.handleChange}
                      onBlur={passwordFormik.handleBlur}
                      value={passwordFormik.values.password}
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
                      name="confirmedPassword"
                      placeholder="repeat new password"
                      onChange={passwordFormik.handleChange}
                      onBlur={passwordFormik.handleBlur}
                      value={passwordFormik.values.confirmedPassword}
                    />
                    <button
                      className={classNames(style.button)}
                      onClick={handlePasswordSubmit}
                    >
                      <FaSave />
                    </button>
                  </>
                ) : (
                  <>
                    <input
                      type="password"
                      value="password1234"
                      disabled={true}
                    />
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
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={classNames(style.message)}>
        {errorMessage ? (
          <div
            className={classNames(style.error, {
              [style.disappear]: disappear,
            })}
          >
            <p>{errorMessage}</p>
          </div>
        ) : null}
        {successMessage ? (
          <div
            className={classNames(style.success, {
              [style.disappear]: disappear,
            })}
          >
            <p>{successMessage}</p>
          </div>
        ) : null}
      </div>
    </>
  );
};
