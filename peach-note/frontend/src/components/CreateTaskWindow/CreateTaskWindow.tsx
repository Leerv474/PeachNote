import React, { useState } from "react";
import style from "./CreateTaskWindow.module.css";
import classNames from "classnames";
import * as yup from "yup";
import CreateTaskProps from "./props/CreateTaskProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { useFormik } from "formik";
import CreateTaskFormikProps from "./props/CreateTaskFormikProps";
import ITaskDto from "../../interfaces/ITaskDto";
import TaskService from "../../services/TaskService";

export const CreateTaskWindow: React.FC<CreateTaskProps> = ({
  boardId,
  setShowCreateTask,
  createTaskProjectId,
  createTaskProjectName,
  triggerTableReload,
  triggerTaskListReload,
  openOrganizeTaskWindow,
}) => {
  const [errorMessage, setError] = useState("");
  const [successMessage, setSuccess] = useState("");
  const [disappear, setDisappear] = useState(false);
  const [openOriganize, setOpenOrganize] = useState(false);

  const handleCreateFetch = async (values: CreateTaskFormikProps) => {
    let data: ITaskDto = {
      title: "unnamed",
      description: "",
      deadline: null,
      projectId: null,
      boardId: boardId,
    };
    data.title = values.title;
    data.description = values.description;
    if (createTaskProjectId !== -1) {
      data.projectId = createTaskProjectId;
    }
    data.deadline = values.deadline ? new Date(values.deadline) : null;
    try {
      const response = await TaskService.create(data);
      const taskData = response.data;
      if (openOriganize) {
        setShowCreateTask(false);
        setOpenOrganize(false);
        openOrganizeTaskWindow(taskData.taskId, taskData.title);
      }
      triggerTableReload((prev) => prev + 1);
      if (createTaskProjectId !== -1) {
        triggerTaskListReload((prev) => prev + 1);
      }
      setSuccess("task created");
      setTimeout(() => {
        setDisappear(true);
      }, 1000);
      setTimeout(() => {
        setSuccess("");
        setDisappear(false);
      }, 1300);
    } catch (error: any) {
      const errorResponse = error.response;
      setError(
        errorResponse?.error ||
          errorResponse?.businessError ||
          "unexpected error",
      );
      setTimeout(() => {
        setDisappear(true);
      }, 1000);
      setTimeout(() => {
        setError("");
        setDisappear(false);
      }, 1300);
    }
  };

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const minDate = today.toISOString().split("T")[0]; // Format to 'YYYY-MM-DD'
  const threeYearsInTheFuture = new Date(today);
  threeYearsInTheFuture.setFullYear(today.getFullYear() + 3);

  const validationSchema = yup.object({
    title: yup
      .string()
      .required()
      .min(2, "title is too short")
      .max(64, "title is too long"),
    deadline: yup
      .string()
      .test(
        "valid deadline",
        "deadline should be in span of three years",
        (deadline) => {
          if (!deadline) {
            return true;
          }
          const dateValue = new Date(deadline);
          dateValue.setHours(0, 0, 0, 0);
          if (dateValue >= threeYearsInTheFuture) {
            return false;
          }
          if (dateValue.getTime() < today.getTime()) {
            return false;
          }
          return true;
        },
      ),
    description: yup.string().max(512, "description is too long"),
  });

  const formik = useFormik<CreateTaskFormikProps>({
    initialValues: {
      title: "",
      deadline: "",
      description: "",
    },
    onSubmit: (values) => {
      handleCreateFetch(values);
    },
    validationSchema,
  });

  const handleSubmit = () => {
    if (formik.isValid) {
      formik.submitForm();
    } else {
      const message =
        formik.errors.title ||
        formik.errors.description ||
        formik.errors.deadline ||
        "";
      setError(message);
      setTimeout(() => {
        setDisappear(true);
      }, 1000);
      setTimeout(() => {
        setError("");
        setDisappear(false);
      }, 1300);
    }
  };
  const handleOrganizeSubmit = () => {
    if (formik.isValid) {
      formik.submitForm();
      setOpenOrganize(true);
    } else {
      const message =
        formik.errors.title ||
        formik.errors.description ||
        formik.errors.deadline ||
        "";
      setError(message);
      setTimeout(() => {
        setDisappear(true);
      }, 1000);
      setTimeout(() => {
        setError("");
        setDisappear(false);
      }, 1300);
    }
  };
  return (
    <>
      <ModelWindow
        setShowWindow={setShowCreateTask}
        windowSizeCss={classNames(style.window_size)}
        successMessage={successMessage}
        errorMessage={errorMessage}
        disappear={disappear}
      >
        <div className={classNames(style.top_bar)}>
          <input
            type="text"
            name="title"
            placeholder="task title"
            className={classNames(style.title_input)}
            onChange={formik.handleChange}
            value={formik.values.title}
            onBlur={formik.handleBlur}
          />
          <input
            type="date"
            name="deadline"
            className={classNames(style.deadline_input)}
            min={minDate}
            onChange={formik.handleChange}
            value={formik.values.deadline}
            onBlur={formik.handleBlur}
          />
        </div>
        <div className={classNames(style.contents)}>
          <p className={classNames(style.description_title)}>Description</p>
          <textarea
            name="description"
            placeholder="write down any important tips for the task..."
            maxLength={512}
            className={classNames(style.description)}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.description}
          />
        </div>
        <div className={classNames(style.bottom_bar)}>
          <div className={classNames(style.buttons_container)}>
            <div className={classNames(style.create_button_container)}>
              <ActionButton label="create" onClick={handleSubmit} />
            </div>
            <div className={classNames(style.organize_button_container)}>
              <ActionButton label="organize" onClick={handleOrganizeSubmit} />
            </div>
          </div>
          {createTaskProjectId !== -1 ? (
            <div className={classNames(style.project)}>
              <p>{createTaskProjectName}</p>
            </div>
          ) : null}
        </div>
      </ModelWindow>
    </>
  );
};
