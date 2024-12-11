export default interface TableProps {
  tableId: number;
  isLastStatus?: boolean;
  openTaskWindow: (id: number) => void;
  tableReload: number;
}
