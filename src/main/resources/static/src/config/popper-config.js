document.addEventListener('DOMContentLoaded', function () {
    // 初始化所有tooltip
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltips].forEach(t => new bootstrap.Tooltip(t));
});