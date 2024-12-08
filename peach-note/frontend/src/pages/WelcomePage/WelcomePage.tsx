import style from "./WelcomePage.module.css";
import React, { useState } from "react";
import classNames from "classnames";
import { AuthButton } from "../../components/ui/AuthButton/AuthButton";
import { useNavigate } from "react-router-dom";

export const WelcomePage: React.FC = () => {
  const navigate = useNavigate();
  const handleRedirect = (isSignIn: boolean) => {
    navigate("/auth", { state: { isSignIn: isSignIn } });
  };
  const [peachFalls, setPeachFalls] = useState(false);
  return (
    <>
      <div className={style.main_card}>
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
        <div className={style.card_container}>
          <div className={classNames(style.card, style.start_card)}>
            <h1>Get Started</h1>
            <div className={style.btn_container}>
              <AuthButton
                label="sign up"
                onClick={() => handleRedirect(false)}
              />
              <AuthButton
                label="sign in"
                onClick={() => handleRedirect(true)}
              />
            </div>
          </div>
          <div className={classNames(style.card, style.slogan_card)}>
            <h1>
              Organize Your Daily
              <br />
              Tasks & Projects now
            </h1>
            <p>To-do manager of the future</p>
          </div>
          <div className={classNames(style.card, style.about_card)}>
            <h1>About</h1>
            <p>
              PeachNote is a note-taking and scheduling application designed
              around the "Getting Things Done" (GTD) methodology. Its primary
              goal is to offer a structured system for managing tasks, whether
              for individuals or teams, to increase productivity and ensure
              clarity in planning and execution.
            </p>
          </div>
          <div className={classNames(style.card, style.features)}>
            <div className={style.features_slogan}>
              <h1>How Does It Work?</h1>
              <p>Apply GTD principles in 5 easy steps.</p>
            </div>
            <div className={style.features_list}>
              <li>
                <b>Capture:</b> Quickly record tasks into the system for later
                categorization.
              </li>
              <li>
                <b>Clarify:</b> Define each task by answering key questions to
                determine its type and priority.
              </li>
              <li>
                <b>Organize:</b> Categorize tasks into boards, status tables, or
                projects.
              </li>
              <li>
                <b>Reflect:</b> Review and update task statuses as progress is
                made.
              </li>
              <li>
                <b>Engage:</b> Execute tasks according to their assigned context
                and priority.
              </li>
            </div>
          </div>
          <div className={classNames(style.card)}>
            <h1>Boiler plate</h1>
            <p>Other promo cards like FAQ, Reasons to use, etc. etc.</p>
          </div>

          <div className={classNames(style.card)}>
            <h1>Boiler plate</h1>
            <p>Other promo cards like FAQ, Reasons to use, etc. etc.</p>
          </div>

          <div className={classNames(style.card)}>
            <h1>Boiler plate</h1>
            <p>Other promo cards like FAQ, Reasons to use, etc. etc.</p>
          </div>

          <div className={classNames(style.card, style.meta)}>
            <h1>Contact</h1>
            <div>
              <h2>Links</h2>
              <li>
                <a
                  href="https://github.com/Leerv474"
                  target="_blank"
                  rel="noreferrer"
                >
                  Github
                </a>
              </li>
              <li>some link</li>
              <li>some link</li>
              <li>some link</li>
            </div>
            <p>Made with &#9829; by Roman Lee</p>
            <p>Pet project</p>
            <h5>
              Copyright © 2024 All Rights Reserved. I've got no licence :P
            </h5>
          </div>
        </div>
      </div>
    </>
  );
};
