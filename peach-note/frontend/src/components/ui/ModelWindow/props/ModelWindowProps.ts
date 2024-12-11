export default interface OverlayWindowProps {
  windowSizeCss?: string;
  setShowWindow: React.Dispatch<React.SetStateAction<boolean>>;
  children?: React.ReactNode;
  errorMessage?: string;
  successMessage?: string;
  disappear?: boolean;
}
