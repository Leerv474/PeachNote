import React, { useContext, useEffect, useState } from "react";
import style from "./SignUpForm.module.css";
import classNames from "classnames";
import { useFormik } from "formik";
import * as yup from "yup";
import SignUpFormikProps from "./props/SighUpFormikProps";
import SignUpProps from "./props/SignUpProps";
import { BiSolidLeaf } from "react-icons/bi";
import { Context } from "../..";

export const SignUpForm: React.FC<SignUpProps> = ({
  setSignIn,
  setShowActivationWindow,
}) => {
  const { store } = useContext(Context);
  const [serverError, setServerError] = useState("");
  const handleSubmit = async (values: any) => {
    const response = await store.signUpUser(values);
    if (response?.error) {
      setServerError(response.error);
      return;
    }
    setServerError("");
    setShowActivationWindow(true);
    setSignIn(true);
  };

  const validationSchema = yup.object({
    username: yup
      .string()
      .required("username is required")
      .min(2, "username is too short")
      .max(24, "username is too long")
      .test("valid username", "invalid username", (username) => {
        return new RegExp("^(?=.*[a-zA-Z])[a-zA-Z0-9_]+$").test(username);
      }),
    email: yup
      .string()
      .email("invalid email format")
      .required("email is required"),
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

  const formik = useFormik<SignUpFormikProps>({
    initialValues: {
      username: "",
      email: "",
      password: "",
      confirmedPassword: "",
    },
    onSubmit: (values) => {
      handleSubmit(values);
    },
    validationSchema,
  });

  const [leafWobble, setLeafWobble] = useState(false);
  useEffect(() => {
    setLeafWobble(Object.entries(formik.errors).length === 0);
    for (const value of Object.values(formik.values)) {
      if (String(value) === "") {
        setLeafWobble(false);
        return;
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [formik.errors]);

  return (
    <div className={style.signup_card}>
      <form onSubmit={formik.handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="username"
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          value={formik.values.username}
        />
        <div className={style.error}>
          {formik.touched.username && formik.errors.username ? (
            <div>*{formik.errors.username}</div>
          ) : null}
        </div>

        <input
          type="email"
          name="email"
          placeholder="email"
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          value={formik.values.email}
        />
        <div className={style.error}>
          {formik.touched.email && formik.errors.email ? (
            <div>*{formik.errors.email}</div>
          ) : null}
        </div>

        <input
          type="password"
          name="password"
          placeholder="password"
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          value={formik.values.password}
        />
        <div className={style.error}>
          {formik.touched.password && formik.errors.password ? (
            <div>*{formik.errors.password}</div>
          ) : null}
        </div>

        <input
          type="password"
          name="confirmedPassword"
          placeholder="repeat password"
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          value={formik.values.confirmedPassword}
        />
        <div className={style.error}>
          {formik.touched.confirmedPassword &&
          formik.errors.confirmedPassword ? (
            <div>*{formik.errors.confirmedPassword}</div>
          ) : null}
        </div>
        <button type="submit">
          <BiSolidLeaf
            className={classNames(style.leaf, { [style.wobble]: leafWobble })}
          />
        </button>
        <div className={classNames(style.server_error)}>
          <p>{serverError}</p>
        </div>
      </form>
    </div>
  );
};
