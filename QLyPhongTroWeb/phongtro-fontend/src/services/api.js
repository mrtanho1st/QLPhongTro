const defaultApiBaseUrl = '/api';

export const apiBaseUrl =
    (import.meta.env.VITE_API_BASE_URL || defaultApiBaseUrl)
        .replace(/\/+$/, '');

export const backendOrigin =
    (import.meta.env.VITE_BACKEND_ORIGIN || window.location.origin)
        .replace(/\/+$/, '');

export async function requestJson(path, options = {}) {

    const response = await fetch(`${apiBaseUrl}${path}`, {
        headers: {
            Accept: 'application/json',
            ...(options.headers || {}),
        },
        ...options,
    });

    if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

export function resolveBackendUrl(value) {

    if (!value) {
        return '';
    }

    const trimmedValue = value.trim();

    // Đã là URL đầy đủ
    if (
        /^https?:\/\//i.test(trimmedValue) ||
        trimmedValue.startsWith('data:') ||
        trimmedValue.startsWith('blob:')
    ) {
        return trimmedValue;
    }

    // Backend luôn trả dạng: /uploads/1/img1.png
    return `${backendOrigin}${trimmedValue}`;
}