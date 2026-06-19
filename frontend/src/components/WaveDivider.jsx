export default function WaveDivider({ className = '', color = '#5B9BD5', flip = false }) {
  return (
    <div className={`w-full overflow-hidden leading-none ${className}`} aria-hidden="true">
      <svg
        viewBox="0 0 1440 80"
        preserveAspectRatio="none"
        className="w-full h-12 md:h-16"
        style={flip ? { transform: 'rotate(180deg)' } : undefined}
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M0,40 C240,80 360,0 720,40 C1080,80 1200,0 1440,40 L1440,80 L0,80 Z" fill={color} opacity="0.18" />
        <path d="M0,52 C240,90 360,14 720,52 C1080,90 1200,14 1440,52 L1440,80 L0,80 Z" fill={color} opacity="0.28" />
      </svg>
    </div>
  );
}