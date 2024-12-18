import React, { useEffect, useState } from "react";
import style from "./TaskWindow.module.css";
import classNames from "classnames";
import * as yup from "yup";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import TaskWindowProps from "./props/TaskWindowProps";
import { useFormik } from "formik";
import SaveTaskFormikProps from "./props/SaveTaskFormikProps";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import TaskService from "../../services/TaskService";
import ITaskDataDto from "../../interfaces/ITaskDataDto";
import { DeleteButton } from "../ui/DeleteButton/DeleteButton";
import ITaskUpdateRequest from "../../interfaces/ITaskUpdateRequest";

export const TaskWindow: React.FC<TaskWindowProps> = ({
  taskId,
  setShowTaskWindow,
  triggerProjectListReload,
  triggerTaskListReload,
  openProjectWindow,
  triggerTableReload,
}) => {
  const [taskData, setTaskData] = useState<ITaskDataDto>();
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await TaskService.viewData(taskId);
        setTaskData(response.data);
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
  }, []);

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

  const handleSaveFetch = async (values: SaveTaskFormikProps) => {
    const requestData: ITaskUpdateRequest = {
      taskId: taskId,
      title: values.title,
      description: values.description,
      deadline: values.deadline,
    };
    if (
      values.title === taskData?.title &&
      values.deadline === (taskData?.deadline?.toDateString() || "") &&
      values.description === taskData.description
    ) {
      return;
    }
    try {
      await TaskService.edit(requestData);
      setSuccessMessage("task updated");
      handleMessageDisappearAnimation();
      triggerTableReload((prev) => prev + 1);
      if (taskData?.projectId !== -1) {
        triggerProjectListReload((prev) => prev + 1);
        triggerTaskListReload((prev) => prev + 1);
      }
      setShowTaskWindow(false);
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

  const handleDeleteFetch = async () => {
    try {
      await TaskService.delete(taskId);
      setSuccessMessage("task deleted");
      handleMessageDisappearAnimation();
      triggerTableReload((prev) => prev + 1);
      if (taskData?.projectId !== -1) {
        triggerProjectListReload((prev) => prev + 1);
        triggerTaskListReload((prev) => prev + 1);
      }
      setShowTaskWindow(false);
    } catch (error: any) {
      const errorMessage = error.response.data;
      setErrorMessage(
        errorMessage?.error || error?.businessError || "unexpected error",
      );
      handleMessageDisappearAnimation();
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

  const formik = useFormik<SaveTaskFormikProps>({
    initialValues: {
      title: taskData?.title || "",
      deadline: taskData?.deadline?.toString() || "",
      description: taskData?.description || "",
    },
    onSubmit: (values) => {
      handleSaveFetch(values);
    },
    validationSchema,
    enableReinitialize: true,
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
      setErrorMessage(message);
      handleMessageDisappearAnimation();
    }
  };

  return (
    <>
      <ModelWindow
        setShowWindow={setShowTaskWindow}
        windowSizeCss={classNames(style.window_size)}
        errorMessage={errorMessage}
        successMessage={successMessage}
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
          <div className={classNames(style.create_button_container)}>
            <ActionButton label="save" onClick={handleSubmit} />
          </div>
          {taskData?.projectId && taskData.projectId !== null ? (
            <div
              className={classNames(style.project)}
              onClick={() => {
                openProjectWindow(taskData.projectId || -1);
              }}
            >
              <p>{taskData.projectTitle}</p>
            </div>
          ) : null}
        </div>
        <DeleteButton
          handleDelete={handleDeleteFetch}
          classname={classNames(style.delete_button)}
        />
      </ModelWindow>
    </>
  );
};
