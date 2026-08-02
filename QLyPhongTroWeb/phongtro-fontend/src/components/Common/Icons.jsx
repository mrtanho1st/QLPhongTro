const iconProps = {
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  strokeWidth: 1.8,
  strokeLinecap: 'round',
  strokeLinejoin: 'round',
  'aria-hidden': 'true',
};

export function BrandIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M4 11.5 12 4l8 7.5" />
      <path d="M6 10.5V20h12v-9.5" />
      <path d="M10 20v-5h4v5" />
    </svg>
  );
}

export function MenuIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M4 7h16" />
      <path d="M4 12h16" />
      <path d="M4 17h16" />
    </svg>
  );
}

export function SearchIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <circle cx="11" cy="11" r="6.5" />
      <path d="m16 16 4 4" />
    </svg>
  );
}

export function HeartIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M20.5 8.3c0 4.8-8.5 11.2-8.5 11.2S3.5 13.1 3.5 8.3A4.8 4.8 0 0 1 12 5.3a4.8 4.8 0 0 1 8.5 3z" />
    </svg>
  );
}

export function UserIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <circle cx="12" cy="8" r="3.5" />
      <path d="M5 20c1.8-3.4 5-5 7-5s5.2 1.6 7 5" />
    </svg>
  );
}

export function MapPinIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M12 21s6-5.5 6-11a6 6 0 1 0-12 0c0 5.5 6 11 6 11z" />
      <circle cx="12" cy="10" r="2.2" />
    </svg>
  );
}

export function FilterIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M4 6h16l-6 7v5l-4 2v-7z" />
    </svg>
  );
}

export function ChevronDownIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="m6 9 6 6 6-6" />
    </svg>
  );
}

export function ChevronLeftIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="m14 6-6 6 6 6" />
    </svg>
  );
}

export function ChevronRightIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="m10 6 6 6-6 6" />
    </svg>
  );
}

export function PhoneIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M5.5 4.5h3l1.5 4-2 1.5a17 17 0 0 0 6 6l1.5-2 4 1.5v3c0 .8-.7 1.5-1.5 1.5C10.5 20 4 13.5 4 5c0-.8.7-1.5 1.5-1.5Z" />
    </svg>
  );
}

export function LocationIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M12 21s5-4.6 5-9a5 5 0 1 0-10 0c0 4.4 5 9 5 9z" />
      <circle cx="12" cy="12" r="1.8" />
    </svg>
  );
}

export function BedIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M4 18V7.5" />
      <path d="M4 12h16v6" />
      <path d="M8 12V9.8c0-.9.7-1.6 1.6-1.6H13c1.8 0 3 1.1 3 2.8V12" />
      <path d="M4 18h16" />
    </svg>
  );
}

export function AreaIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M7 7h10v10H7z" />
      <path d="M7 12h10" />
      <path d="M12 7v10" />
    </svg>
  );
}

export function CalendarIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <rect x="4" y="5" width="16" height="15" rx="2" />
      <path d="M8 3v4M16 3v4M4 9h16" />
    </svg>
  );
}

export function CloseIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="m6 6 12 12M18 6 6 18" />
    </svg>
  );
}

export function HomeIcon(props) {
  return (
    <svg {...iconProps} {...props}>
      <path d="M4 11.5 12 4l8 7.5" />
      <path d="M6 10.5V20h12v-9.5" />
    </svg>
  );
}