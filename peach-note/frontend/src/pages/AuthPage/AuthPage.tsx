import React, { useState } from "react";
import style from "./AuthPage.module.css";
import classNames from "classnames";
import { TiArrowSortedUp } from "react-icons/ti";
import { SignUpForm } from "../../components/SignUpForm/SignUpForm";
import { SignInForm } from "../../components/SignInForm/SignInForm";
import { useLocation, useNavigate } from "react-router-dom";
import { ActivationWindow } from "../../components/ui/ActivationWindow/ActivationWindow";

export const AuthPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const state = location.state as { isSignIn: boolean };
  const [isSighIn, setSignIn] = useState(
    state === null ? true : state.isSignIn,
  );
  const [peachFalls, setPeachFalls] = useState(false);

  const [showActivationWindow, setShowActivationWindow] = useState(false);
  return (
    <>
      <div className={classNames(style.main_card)}>
        <div
          className={style.title_box}
          onClick={() => {
            navigate("/");
          }}
        >
          <h1>
            PeachN
            <img
              src="./peachnote-icon.png"
              alt="PEACH"
              className={classNames(
                { [style.wobble]: !peachFalls },
                { [style.peach_falls]: peachFalls },
              )}
              onClick={(e) => {
                e.stopPropagation();
                setPeachFalls(true);
                setTimeout(() => setPeachFalls(false), 2000);
              }}
            />
            te
          </h1>
        </div>
        <div className={classNames(style.option_bar)}>
          {isSighIn ? (
            <div
              className={classNames(style.signup_button)}
              onClick={() => setSignIn(false)}
            >
              <TiArrowSortedUp className={classNames(style.arrow_left)} />
              <p>sign up</p>
            </div>
          ) : null}
          {isSighIn ? (
            <p className={classNames(style.title)}>sign in</p>
          ) : (
            <p className={classNames(style.title)}>sign up</p>
          )}
          {!isSighIn ? (
            <div
              className={classNames(style.signin_button)}
              onClick={() => setSignIn(true)}
            >
              <p>sign in</p>
              <TiArrowSortedUp className={classNames(style.arrow_right)} />
            </div>
          ) : null}
        </div>
        {isSighIn ? (
          <SignInForm />
        ) : (
          <SignUpForm
            setSignIn={setSignIn}
            setShowActivationWindow={setShowActivationWindow}
          />
        )}
        {showActivationWindow ? (
          <ActivationWindow
            setShowActivationWindow={setShowActivationWindow}
            length={6}
            showActivationWindow={showActivationWindow}
          ></ActivationWindow>
        ) : null}
      </div>
    </>
  );
};
