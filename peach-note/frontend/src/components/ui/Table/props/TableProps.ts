export default interface TableProps {
  tableId: number;
  isLastStatus?: boolean;
  isFirstStatus?: boolean;
  openTaskWindow: (id: number) => void;
  openOrganizeWindow: (id: number, title: string) => void;
  triggerTableReload: React.Dispatch<React.SetStateAction<number>>;
  tableReload: number;
}
