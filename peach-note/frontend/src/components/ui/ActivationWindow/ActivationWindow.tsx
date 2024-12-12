import React, { useContext, useEffect, useRef, useState } from "react";
import style from "./ActivationWindow.module.css";
import classNames from "classnames";
import { ModelWindow } from "../ModelWindow/ModelWindow";
import ActivationWindowProps from "./props/ActivationWindowProps";
import { Context } from "../../..";

export const ActivationWindow: React.FC<ActivationWindowProps> = ({
  length,
  showActivationWindow,
  setShowActivationWindow,
}) => {
  const [error, setError] = useState("");
  const { store } = useContext(Context);
  const [inputs, setInputs] = useState<string[]>(Array(length).fill(""));
  const refs = useRef<(HTMLInputElement | null)[]>(Array(length).fill(null));

  useEffect(() => {
    if (showActivationWindow) {
      refs.current[0]?.focus();
    } else {
      clearInputs();
      setError("");
    }
  }, [showActivationWindow]);

  const clearInputs = () => {
    setInputs((prev) => prev.map(() => ""));
  };

  const findCurrentStateValue = (index: number, eventValue: string) => {
    for (let i = 0; i < inputs.length; i++) {
      if (i !== index) {
        continue;
      }
      if (inputs[i] === "") {
        return eventValue;
      }
      if (eventValue !== "") {
        if (eventValue[0] === inputs[i]) {
          return eventValue[1];
        }
        return eventValue[0];
      }
      return eventValue;
    }
    return "";
  };

  return (
    <>
      <ModelWindow
        setShowWindow={setShowActivationWindow}
        windowSizeCss={classNames(style.window_size)}
      >
        <div className={classNames(style.container)}>
          <div className={style.inputs}>
            {inputs.map((input, index) => (
              <input
                key={index}
                ref={(ref) => (refs.current[index] = ref)}
                type="text"
                value={input}
                onChange={(event) => {
                  setError("");
                  const eventValue = event.target.value;
                  let currentStateValue = findCurrentStateValue(
                    index,
                    eventValue,
                  );
                  setInputs((prev) =>
                    prev.map((inputValue, inputIndex) =>
                      inputIndex === index ? currentStateValue : inputValue,
                    ),
                  );
                  const nextRef = refs.current[index + 1];
                  if (eventValue.length !== 2 && nextRef) {
                    nextRef.focus();
                  }
                  const stringCode = inputs
                    .map((inputValue, inputIndex) =>
                      inputIndex === index ? currentStateValue : inputValue,
                    )
                    .join("");
                  if (stringCode.length === inputs.length) {
                    if (!new RegExp("^[0-9]{6}$").test(stringCode)) {
                      setError("Incorrect code format");
                    } else {
                      store.activateAccount(
                        stringCode,
                        refs.current,
                        clearInputs,
                        setError,
                        setShowActivationWindow,
                      );
                    }
                  }
                }}
              />
            ))}
          </div>
          <div className={style.error}>
            <p>{error}</p>
          </div>
        </div>
      </ModelWindow>
    </>
  );
};
