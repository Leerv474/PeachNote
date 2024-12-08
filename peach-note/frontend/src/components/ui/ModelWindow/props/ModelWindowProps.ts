export default interface OverlayWindowProps {
  windowSizeCss?: string;
  setShowWindow: React.Dispatch<React.SetStateAction<boolean>>;
  children?: React.ReactNode;
}
