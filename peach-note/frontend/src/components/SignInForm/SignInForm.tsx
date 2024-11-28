import React, { useEffect, useState } from "react";
import style from "./SignInForm.module.css";
import classNames from "classnames"
import { useFormik } from "formik";
import * as yup from "yup";
import SignInFormikProps from "./props/SignInFormikProps";
import { BiSolidLeaf } from "react-icons/bi";

export const SignInForm: React.FC = () => {
  const validationSchema = yup.object({
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
  });

  const formik = useFormik<SignInFormikProps>({
    initialValues: {
      email: "",
      password: "",
    },
    onSubmit: (values) => {
      console.log(values);
    },
    validationSchema,
  });

  const [leafWobble, setLeafWobble] = useState(false);
  useEffect(() => {
    setLeafWobble(Object.entries(formik.errors).length === 0)
    for (const value of Object.values(formik.values)) {
      if (String(value) === "") {
        setLeafWobble(false);
        return;
      }
    }
    console.log(Object.entries(formik.errors).length === 0)
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [formik.errors]);

  return (
    <div className={style.signin_card}>
      <form onSubmit={formik.handleSubmit}>
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
        <button type="submit">
          <BiSolidLeaf className={classNames(style.leaf, {[style.wobble] : leafWobble})} />
        </button>
      </form>
    </div>
  );
};
