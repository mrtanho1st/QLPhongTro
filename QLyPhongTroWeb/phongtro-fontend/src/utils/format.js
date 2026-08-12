export function formatCurrency(value) {
  const numberValue = Number(value || 0);

  return `${new Intl.NumberFormat('vi-VN', {
    maximumFractionDigits: 0,
  }).format(numberValue)}đ`;
}

export function formatNumberInput(value) {
    const number = value.replace(/\D/g, '');

    if (!number) {
        return '';
    }

    return new Intl.NumberFormat('vi-VN').format(Number(number));
}

export function formatArea(value) {
  const numberValue = Number(value || 0);
  return `${new Intl.NumberFormat('vi-VN', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 2,
  }).format(numberValue)} m²`;
}

export function formatDate(value) {
  if (!value) {
    return 'Trống ngay';
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return String(value);
  }

  return new Intl.DateTimeFormat('vi-VN').format(date);
}

export function toText(value) {
  if (value === null || value === undefined) {
    return '';
  }

  return String(value);
}

export function clamp(value, min, max) {
  return Math.min(Math.max(value, min), max);
}