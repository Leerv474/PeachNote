import ITaskOrganizeData from "../../../interfaces/ITaskOrganizeData";

export default interface OrganizeWindowProps {
  taskData: ITaskOrganizeData | undefined;
  setShowOrganizeWindow: React.Dispatch<React.SetStateAction<boolean>>;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  triggerProjectListReload: React.Dispatch<React.SetStateAction<number>>;
  triggerTaskListReload: React.Dispatch<React.SetStateAction<number>>;
}
