export default interface TableProps {
  title: string;
  isLastStatus?: boolean;
  openTaskWindow: (id: number) => void;
}
