import React, { useEffect, useState } from "react";
import style from "./ProjectWindow.module.css";
import classNames from "classnames";
import * as yup from "yup";
import ProjectWindowProps from "./props/ProjectWindowProps";
import { ModelWindow } from "../ui/ModelWindow/ModelWindow";
import { DeleteButton } from "../ui/DeleteButton/DeleteButton";
import IProjectDataDto from "../../interfaces/IProjectDataDto";
import SaveProjectFormikProps from "./props/SaveProjectFormikProps";
import { useFormik } from "formik";
import { ActionButton } from "../ui/ActionButton/ActionButton";
import { TaskProjectItem } from "../ui/TaskProjectItem/TaskProjectItem";
import ProjectService from "../../services/ProjectService";
import IProjectEditRequest from "../../interfaces/IProjectEditRequest";

export const ProjectWindow: React.FC<ProjectWindowProps> = ({
  projectId,
  setShowProject,
  openTaskWindow,
  taskListReload,
  triggerTaskListReload,
  triggerProjectListReload,
  triggerTableReload,
  openCreateTaskWindow,
  openOrganizeTaskWindow,
}) => {
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await ProjectService.view(projectId);
        setProjectData(response.data);
        console.log(response.data);
      } catch (error: any) {
        const errorMessage = error.response.data;
        setErrorMessage(
          errorMessage?.error ||
            errorMessage?.businessError ||
            "unexpected error",
        );
      }
    };
    fetchData();
  }, [projectId, taskListReload]);
  const [projectData, setProjectData] = useState<IProjectDataDto>();

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
  const handleDeleteFetch = async () => {
    try {
      await ProjectService.delete(projectId);
      setShowProject(false);
      triggerProjectListReload((prev) => prev + 1);
      if (projectData?.taskList) {
        triggerTableReload((prev) => prev + 1);
      }
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
  const handleSaveFetch = async (values: SaveProjectFormikProps) => {
    const request: IProjectEditRequest = {
      projectId: projectId,
      title: values.title,
      description: values.description,
      deadline: values.deadline,
    };
    if (
      values.title === projectData?.title &&
      values.deadline === (projectData?.deadline || "") &&
      values.description === projectData?.description
    ) {
      setShowProject(false);
      return;
    }
    try {
      await ProjectService.edit(request);
      setSuccessMessage("project saved");
      handleMessageDisappearAnimation();
      triggerProjectListReload((prev) => prev + 1);
      setShowProject(false);
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

  const formik = useFormik<SaveProjectFormikProps>({
    initialValues: {
      title: projectData?.title || "",
      deadline: projectData?.deadline?.toString() || "",
      description: projectData?.description || "",
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
        setShowWindow={setShowProject}
        windowSizeCss={classNames(style.window_size)}
        errorMessage={errorMessage}
        successMessage={successMessage}
        disappear={disappear}
      >
        <div className={classNames(style.task_list_container)}>
          <div className={classNames(style.task_list_title_container)}>
            <p className={classNames(style.task_list_title)}>project tasks</p>
          </div>
          <div className={classNames(style.task_list_box)}>
            <div className={classNames(style.task_list_scroll_container)}>
              {projectData?.taskList ? (
                projectData?.taskList
                  .sort((a, b) => a.priority - b.priority)
                  .map((task, index) => (
                    <TaskProjectItem
                      key={index}
                      taskId={task.taskId}
                      title={task.title}
                      deadline={task.deadline}
                      statusName={task.statusTable.name}
                      openTaskWindow={openTaskWindow}
                      triggerTableReload={triggerTableReload}
                      triggerTaskListReload={triggerTaskListReload}
                      setErrorMessage={setErrorMessage}
                      setSuccessMessage={setSuccessMessage}
                      openOrganizeTaskWindow={openOrganizeTaskWindow}
                      handleMessageDisappearAnimation={
                        handleMessageDisappearAnimation
                      }
                    />
                  ))
              ) : (
                <p className={classNames(style.empty_message)}>
                  the list is empty
                </p>
              )}
            </div>
            <div className={classNames(style.task_button_container)}>
              <div className={classNames(style.task_button_flex_container)}>
                <ActionButton
                  label={"new task"}
                  onClick={() =>
                    openCreateTaskWindow(projectId, projectData?.title || "???")
                  }
                />
              </div>
            </div>
          </div>
        </div>
        <div className={classNames(style.project_data_container)}>
          <div className={classNames(style.top_bar)}>
            <input
              type="text"
              name="title"
              placeholder="project title"
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
              placeholder="write down any important tips for the project..."
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
          </div>
        </div>
        <DeleteButton
          handleDelete={handleDeleteFetch}
          classname={classNames(style.delete_button)}
        />
      </ModelWindow>
    </>
  );
};
